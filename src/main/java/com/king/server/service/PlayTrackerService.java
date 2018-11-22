package com.king.server.service;

import com.king.model.profile.User;
import com.king.server.service.event.ScoreEventListener;

/**
 * TrackerService to track game play activity like scores
 */
public interface PlayTrackerService extends ScoreEventListener {

	/**
	 * Record the user score
	 * 
	 * @param level
	 * @param score
	 */
	public void recordScore(final User user, final int level, final int score);

}
