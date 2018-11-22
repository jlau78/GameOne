package com.king.server.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.king.model.profile.Session;
import com.king.model.profile.User;;

public class SimpleLoginService implements LoginService {

	private static Logger LOGGER = Logger.getLogger(SimpleLoginService.class.getName());

	// User sessions holder. Kept inline for simplicity. TODO: Move out to a
	// separate repository.
	private Map<String, Long> userSessionsMap = new HashMap<>();

	private static final long SESSION_EXPIRE_MS = 600000;
	private static final int RANDOM_SEED_BOUND = 100000;

	public static SimpleLoginService loginService;

	private SimpleLoginService() {

	}

	public static synchronized SimpleLoginService getInstance() {
		if (loginService == null) {
			loginService = new SimpleLoginService();
		}
		return loginService;
	}

	@Override
	public User login(final String cred) {
		User user = null;

		// Test system. Login auth not implemented. Just create a new session
		if (cred != null) {
			user = newSession(cred);
		}

		return user;
	}

	@Override
	public boolean logout(final String sessionid) {
		return removeSession(sessionid);
	}

	@Override
	public boolean authenticated(final String sessionid) {
		boolean valid = false;
		Long timestamp = userSessionsMap.get(sessionid);

		if (timestamp != null && System.currentTimeMillis() - timestamp < SESSION_EXPIRE_MS) {
			valid = true;
		}
		return valid;
	}

	private User newSession(final String cred) {
		String unqid = cred + String.valueOf(new Random().nextInt(RANDOM_SEED_BOUND));
		addSession(cred, String.valueOf(unqid));

		User user = new User();
		String[] credentials = cred.split("\\|");
		user.setName(credentials[0]);
		user.setPwd(credentials[1]);
		Session session = user.getSession();
		session.setId(unqid);

		LOGGER.log(Level.INFO,
				"New session created for user cred: " + cred + ", sessionid:" + user.getSession().getId());

		return user;

	}

	private boolean addSession(final String cred, final String sessionId) {
		boolean success = false;
		if (userSessionsMap.get(sessionId) == null) {
			userSessionsMap.put(sessionId, System.currentTimeMillis());
			success = true;
		}
		return success;
	}

	private boolean removeSession(final String sessionid) {
		boolean success = false;
		if (userSessionsMap.get(sessionid) != null) {
			userSessionsMap.remove(sessionid);
			success = true;
		}
		return success;
	}

	protected void printSessions() {
		userSessionsMap.forEach((k, v) -> System.out.println(k + "," + v));
	}

}
