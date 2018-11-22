package com.king.server.service;

import com.king.model.profile.User;

public interface LoginService {

	/**
	 * Login the given credentials
	 * 
	 * @param cred
	 * @return User bean
	 */
	public User login(final String cred);

	/**
	 * Logout the sessionid
	 * 
	 * @param sessionid
	 */
	public boolean logout(final String sessionid);

	/**
	 * Check if the sessionid is still valid
	 * 
	 * @param sessionid
	 * @return True if sessionid is valid
	 */
	public boolean authenticated(final String sessionid);

}
