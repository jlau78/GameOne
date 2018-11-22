package com.king.model.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.king.model.profile.Score;

/**
 * Repository to hold the Map of scores
 *
 */
public class ScoreRepository implements MapRepository<String, Score> {

	private Map<String, Score> scoresMap = new ConcurrentHashMap<>();

	public static ScoreRepository scoreHolder;

	private ScoreRepository() {
	}

	public static synchronized ScoreRepository getInstance() {
		if (scoreHolder == null) {
			scoreHolder = new ScoreRepository();
		}
		return scoreHolder;
	}

	@Override
	public boolean update(String key, Score value) {
		boolean success = false;

		if (key != null) {
			this.scoresMap.put(key, value);
		}

		return success;
	}

	@Override
	public boolean remove(final String key) {
		boolean success = false;

		if (key != null) {
			this.scoresMap.remove(key);
		}

		return success;
	}

	@Override
	public Score find(String key) {
		Score s = null;
		if (key != null) {
			s = this.scoresMap.get(key);
		}
		return s;
	}

	public Map<String, Score> getAll() {
		return scoresMap;
	}
}
