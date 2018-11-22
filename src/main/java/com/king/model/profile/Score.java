package com.king.model.profile;

import java.util.ArrayList;
import java.util.List;

public class Score {

	private int maxScoresKept = 100;
	private int level;
	private List<Integer> scores = new ArrayList<>();
	private long timestamp;

	public Score(final int level, final int score) {
		addScore(level, score);
	}

	/**
	 * Add the score for the given level. We only store the highest scores in the
	 * list.
	 * 
	 * @param level
	 * @param score
	 */
	public void addScore(final int level, final int score) {
		this.level = level;

		// sort asc and add higher score
		scores.sort((s1, s2) -> s1 - s2);

		if (scores.size() < getMaxScoresKept()) {
			scores.add(score);
		} else {
			if (scores.get(0) < score) {
				scores.remove(0);
				scores.add(score);
			}
		}
	}

	protected int getMaxScoresKept() {
		return maxScoresKept;
	}

	protected void setMaxScoresKept(int maxScoresKept) {
		this.maxScoresKept = maxScoresKept;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setScores(final List<Integer> scores) {
		this.scores = scores;
	}

	public List<Integer> getScores() {
		return this.scores;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
