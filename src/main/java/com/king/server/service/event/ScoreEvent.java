package com.king.server.service.event;

import com.king.model.profile.User;

public class ScoreEvent {

	private User user;
	private int level;
	private int score;

	public ScoreEvent(final User user, final int level, final int score) {
		this.user = user;
		this.level = level;
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
