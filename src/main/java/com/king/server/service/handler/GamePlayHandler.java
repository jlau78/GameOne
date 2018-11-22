package com.king.server.service.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.king.client.Game;
import com.king.client.GameOne;
import com.king.model.profile.User;
import com.king.server.LifetimeThreadExcutorService;

/**
 * Handler to invoke game play for each of the given players
 * 
 */
public class GamePlayHandler implements PayloadHandlerInterface<Map<String, String>> {

	Logger LOGGER = Logger.getLogger(GamePlayHandler.class.getName());

	public static GamePlayHandler handler;

	private LifetimeThreadExcutorService<Boolean> executor;

	private GamePlayHandler() {
	}

	public static synchronized GamePlayHandler getInstance() {
		if (handler == null) {
			handler = new GamePlayHandler();
		}
		return handler;
	}

	/**
	 * Start a game play for for each of the given players.
	 * 
	 * @param players Map of players
	 */
	public void handle(final Map<String, String> players) {

		final Game game = GameOne.getInstance();
		final Map<String, String> gamePlayers = players;

		// Foreach user start a game simultaneously
		List<Callable<Boolean>> callables = new ArrayList<>();

		gamePlayers.forEach((k, v) -> callables.add(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Boolean result = playGame(game, k, v);
				return result;
			}
		}));

		LifetimeThreadExcutorService<Boolean> executor = getExecutor();
		executor.submit(callables, 60);
	}

	protected Boolean playGame(final Game game, final String name, final String password) {

		User user = game.start(name, password);
		if (user != null) {
			game.play(user);
		} else {
			LOGGER.log(Level.SEVERE, "User " + name + " could not login and play the game!");
		}

		return Boolean.TRUE;
	}

	@SuppressWarnings("unchecked")
	public LifetimeThreadExcutorService<Boolean> getExecutor() {
		if (null == executor) {
			executor = LifetimeThreadExcutorService.getInstance();
		}
		return executor;
	}

}
