package com.king.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.king.model.profile.User;
import com.king.server.service.LoginService;
import com.king.server.service.PlayTrackerService;
import com.king.server.service.PlayTrackerServiceImpl;
import com.king.server.service.SimpleLoginService;
import com.king.server.service.event.ScoreEvent;
import com.king.server.service.event.ScoreEventListener;
import com.king.utils.Utils;

public class GameOne implements Game {

	private static Logger LOGGER = Logger.getLogger(GameOne.class.getName());

	private long simulate_play_pause_ms = 1000;

	private int maxGameLevelsAllowed = 6;
	private int maxLevelPlay = 25;

	public static GameOne game;

	private PlayTrackerService tracker;
	private LoginService loginService;

	private List<ScoreEventListener> scoreEventListeners = new ArrayList<>();

	private GameOne() {
		Properties configs = Utils.readGlobalConfigs();

		this.simulate_play_pause_ms = Long.valueOf((String) configs.get("simulateLevelPlayPause"));
		this.maxGameLevelsAllowed = Integer.valueOf((String) configs.get("maxGameLevelsAllowed"));
		this.maxLevelPlay = Integer.valueOf((String) configs.get("maxLevelPlay"));

		scoreEventListeners.add(getTracker());
	}

	public static synchronized GameOne getInstance() {
		if (game == null) {
			game = new GameOne();
		}
		return game;
	}

	@Override
	public User start(String name, String password) {

		User user = getLoginService().login(Utils.buildLoginCredentials(name, password));
		if (user == null) {
			LOGGER.log(Level.SEVERE, "Fail to login user:".concat(name));
		}

		return user;
	}

	@Override
	public void play(User user) {

		if (user != null) {
			boolean valid = getLoginService().authenticated(user.getSession().getId());
			if (valid) {
				LOGGER.log(Level.INFO, "Starting play for user:" + user.getName());

				boolean continueGame = true;
				while (continueGame) {
					score(user);
					sendScore(user, user.getCurrentLevel(), user.getCurrentScore());
					levelProgress(user);

					continueGame = user.getCurrentLevel() <= maxGameLevelsAllowed;
					if (!continueGame) {
						stop(user);
					}

					try {
						Thread.sleep(this.simulate_play_pause_ms);
					} catch (InterruptedException e) {
						LOGGER.log(Level.INFO, "Interrupted sleep", e);
					}
				}
			} else {
				LOGGER.log(Level.INFO, "Cannot play game!! User not authenticated:" + user.getName());
			}

		}
	}

	@Override
	public void stop(final User user) {
		if (user != null) {
			getLoginService().logout(user.getSession().getId());
//			((PlayTrackerServiceImpl) this.tracker).printScores();
			LOGGER.log(Level.INFO, "User completed all game levels!!! " + user.getName());
		}

	}

	private void levelProgress(User user) {
		// Complete level if level time elapsed
		long levelElapsed = System.currentTimeMillis() - user.getSession().getStartTime();

		user.setCurrentLevelPlayed(user.getCurrentLevelPlayed() + 1);
		if (user.getCurrentLevelPlayed() > maxLevelPlay) {
			upLevel(user);
		} else {
			// Play same level again
			score(user);
			sendScore(user, user.getCurrentLevel(), user.getCurrentScore());
		}

	}

	private void upLevel(User user) {
		if (user != null) {
			user.setCurrentLevel(user.getCurrentLevel() + 1);
			user.setCurrentLevelPlayed(1);
			score(user);
			sendScore(user, user.getCurrentLevel(), user.getCurrentScore());

			LOGGER.log(Level.INFO, "User " + user.getName() + " moved up to next level:" + user.getCurrentLevel());

//			printAllScores();
			printUserCurrentLevelScores(user, user.getCurrentLevel() - 1);

		}

	}

	@Override
	public void score(User user) {
		int newScore = simulateScore(user.getCurrentScore());
		user.setCurrentScore(newScore);
	}

	@Override
	public boolean sendScore(final User user, final int level, final int score) {
		if (score > 0) {
			scoreEventListeners.forEach(se -> se.scoreUpdate(new ScoreEvent(user, level, score)));

			LOGGER.log(Level.FINER, "Score sent for user:{0}, level:{1} score:{2}",
					new Object[] { user.getName(), String.valueOf(level), String.valueOf(score) });
		}
		return true;
	}

	/*
	 * Simulate game play with a random score
	 */
	private int simulateScore(final int score) {

		int curScore = score;

		Random rnd = new Random();
		curScore += rnd.nextInt(113);

		return curScore;
	}

	public void printAllScores() {

		((PlayTrackerServiceImpl) getTracker()).printScores();
	}

	public void printUserCurrentLevelScores(final User user, final int level) {

		((PlayTrackerServiceImpl) getTracker()).printUserCurrentLevelScores(user, level);
	}

	public LoginService getLoginService() {
		if (null == this.loginService) {
			setLoginService(SimpleLoginService.getInstance());
		}
		return this.loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public PlayTrackerService getTracker() {
		if (null == this.tracker) {
			setTracker(PlayTrackerServiceImpl.getInstance());
		}
		return tracker;
	}

	public void setTracker(PlayTrackerService tracker) {
		this.tracker = tracker;
	}

}
