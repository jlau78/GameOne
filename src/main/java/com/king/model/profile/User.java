package com.king.model.profile;

import java.util.List;

public class User {

	private String name;
	private String pwd;

	private Session session;
	private int currentLevel = 1;
	private int currentLevelPlayed = 0;
	private int currentScore;
	private List<Score> levelScores;

	public String getCredentials(final String name, final String pwd) {
		return name + pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Session getSession() {
		if (this.session == null) {
			session = new Session(currentLevel, currentScore);
		}
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public int getCurrentLevelPlayed() {
		return currentLevelPlayed;
	}

	public void setCurrentLevelPlayed(int currentLevelPlayed) {
		this.currentLevelPlayed = currentLevelPlayed;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public void setLevelScores(List<Score> levelScores) {
		this.levelScores = levelScores;
	}

	public List<Score> getLevelScores() {
		return this.levelScores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pwd == null) ? 0 : pwd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pwd == null) {
			if (other.pwd != null)
				return false;
		} else if (!pwd.equals(other.pwd))
			return false;
		return true;
	}

}
