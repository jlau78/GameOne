package com.king;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.omg.CORBA.SystemException;

import com.king.client.Game;
import com.king.server.service.handler.GamePlayHandler;
import com.king.server.service.handler.PayloadHandlerInterface;
import com.king.utils.Utils;

public class Application {
	protected static Logger LOGGER = Logger.getLogger(Application.class.getName());

	protected Game game;
	private static PayloadHandlerInterface<Map<String, String>> gamePlayer;

	Map<String, String> players = new HashMap<>();

	public Application() {

//		players.put("John", "pass");
//		players.put("Paul", "pass");
//		players.put("Gary", "pass");
//		players.put("Smurf", "pass");

		players = Utils.getPlayers();

	}

	protected Map<String, String> getPlayers() {
		return players;
	}

	protected void setPlayers(Map<String, String> players) {
		this.players = players;
	}

	public static void main(String[] args) throws SystemException {

		Application application = new Application();

		gamePlayer = GamePlayHandler.getInstance();
		gamePlayer.handle(application.getPlayers());
	}

}
