package com.king.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.king.model.profile.User;

/**
 * Static utility functions
 *
 */
public class Utils {

	public static Logger LOGGER = Logger.getLogger(Utils.class.getName());

	public static final String GLOBAL_CONFIG_FILE = "config/config.properties";
	public static final String PLAYERS_INPUT_FILE = "data/players.properties";

	/**
	 * Build the login credentials with the expected format
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public static String buildLoginCredentials(final String name, final String password) {
		return name.concat(Constants.DELIMITER_PIPE).concat(password);

	}

	/**
	 * Update the given User with the credentials. See
	 * {@link Utils.buildLoginCredentials}
	 * 
	 * @param user
	 * @param cred
	 */
	public static void updateUserDetails(final User user, final String cred) {

		String[] c = cred.split(Constants.DELIMITER_PIPE_REGEX);
		user.setName(c[0]);
		user.setPwd(c[1]);
	}

	/**
	 * Get the global config properties
	 * 
	 * @return
	 */
	public static Properties readGlobalConfigs() {
		return getPropertiesFromFile(GLOBAL_CONFIG_FILE);
	}

	private static Properties getPropertiesFromFile(final String file) {

		Properties prop = new Properties();
		InputStream in = null;

		try {
			in = new FileInputStream(file);
			prop.load(in);

		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "FAIL to load the given config file:" + file);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "FAIL to close the streams");
				}
			}
		}

		return prop;
	}

	public static Map<String, String> getPlayers() {
		List<String> inputs = readInputFromFile(PLAYERS_INPUT_FILE);

		Map<String, String> players = inputs.stream().filter(p -> p.indexOf(Constants.DELIMITER_PIPE) > -1)
				.map(p -> p.split(Constants.DELIMITER_PIPE_REGEX)).collect(Collectors.toMap(e -> e[0], e -> e[1]));

		return players;
	}

	private static List<String> readInputFromFile(final String file) {
		List<String> inputs = new ArrayList<>();
		InputStream in = null;
		BufferedReader br = null;

		try {
			in = new FileInputStream(file);

			br = new BufferedReader(new InputStreamReader(in));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line != null || line.trim().length() > 0) {
					inputs.add(line);
				}
			}

		} catch (IOException e) {

		} finally {
			if (in != null && br != null) {
				try {
					in.close();
					br.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "FAIL to close the streams");
				}
			}
		}

		return inputs;
	}

	/**
	 * Build the key to search the ScoreRepository data
	 * 
	 * @param user
	 * @param level
	 * @return
	 */
	public static String buildScoreKey(final User user, final int level) {
		String userid = user.getName().concat("_").concat(user.getSession().getId());
		return userid.concat("_").concat(String.valueOf(level));

	}

	public final static class Constants {

		public static final String DELIMITER_PIPE = "|";
		public static final String DELIMITER_PIPE_REGEX = "\\|";

		public static final String LOG_TITLE_BORDER = "**************************************************************";

	}

}
