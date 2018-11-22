package com.king.client;

import com.king.model.profile.User;

public interface Game {

	/**
	 * Start the user game
	 * 
	 * @param name
	 * @param password
	 * @return User bean after login
	 */
	public User start(final String name, final String password);

	/**
	 * User to play the current level
	 * 
	 * @param user
	 */
	public void play(User user);

	/**
	 * Stop user play
	 * 
	 * @param user
	 */
	public void stop(User user);

	/**
	 * Score the play for the user
	 * 
	 * @param user
	 * @param level
	 */
	public void score(final User user);

	/**
	 * Send the score
	 * 
	 * @param level
	 * @param score
	 * @return
	 */
	public boolean sendScore(final User user, final int level, final int score);

}
