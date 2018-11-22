package com.king.server.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.king.model.profile.Score;
import com.king.model.profile.User;
import com.king.model.repository.MapRepository;
import com.king.model.repository.ScoreRepository;
import com.king.server.service.event.ScoreEvent;
import com.king.utils.Utils;
import com.king.utils.Utils.Constants;;

public class PlayTrackerServiceImpl implements PlayTrackerService {

	private static Logger LOGGER = Logger.getLogger(PlayTrackerServiceImpl.class.getName());
	private static final int MAX_SCORE_RESULT_COUNT = 15;

	public static PlayTrackerServiceImpl playTrackerService;

	private MapRepository<String, Score> scoresRepository;
	private LoginService loginService;

	private PlayTrackerServiceImpl() {
		scoresRepository = ScoreRepository.getInstance();
		loginService = SimpleLoginService.getInstance();
	}

	public static synchronized PlayTrackerServiceImpl getInstance() {
		if (playTrackerService == null) {
			playTrackerService = new PlayTrackerServiceImpl();
		}
		return playTrackerService;
	}

	@Override
	public void scoreUpdate(final ScoreEvent event) {
		recordScore(event.getUser(), event.getLevel(), event.getScore());
	}

	/**
	 * Record score if user is still authenticated
	 */
	@Override
	public void recordScore(final User user, final int level, final int score) {
		if (user != null) {
			boolean valid = this.loginService.authenticated(user.getSession().getId());
			if (valid) {
				Score scoreObj = ScoreQueryTool.getScore(user, level);
				if (scoreObj == null) {
					scoreObj = new Score(level, score);
				} else {
					scoreObj.addScore(level, score);
				}
				this.scoresRepository.update(Utils.buildScoreKey(user, level), scoreObj);
			}
		}
	}

	/**
	 * Print all the scores from the ScoreRepository
	 */
	public void printScores() {

		LOGGER.log(Level.INFO, "");
		LOGGER.log(Level.INFO, Constants.LOG_TITLE_BORDER);
		LOGGER.log(Level.INFO, "         SCORES SUMMARY: "
				+ DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()) + "             ");
		LOGGER.log(Level.INFO, Constants.LOG_TITLE_BORDER);

		Map<String, Score> sortedScores = scoresRepository.getAll().entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		sortedScores.forEach((k, v) -> System.out
				.println(k + ", level:" + String.valueOf(v.getLevel()) + ", scores:" + v.getScores().toString()));

		LOGGER.log(Level.INFO, "");

	}

	public void printUserCurrentLevelScores(final User user, final int level) {

		List<Integer> sortedScores = ScoreQueryTool.getScoreDesc(user, level);

		List<Integer> limitedScores = sortedScores.stream()
				.filter(t -> sortedScores.indexOf(t) < MAX_SCORE_RESULT_COUNT)
				.collect(Collectors.toCollection(ArrayList::new));

		LOGGER.log(Level.INFO, "");
		LOGGER.log(Level.INFO,
				"#### Previous Level scores: "
						+ DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()) + ", User:"
						+ user.getName() + ", level:" + level + "\n    " + limitedScores);
		LOGGER.log(Level.INFO, "");

	}

}
