package com.king.server.service;

import java.util.List;

import com.king.model.profile.Score;
import com.king.model.profile.User;
import com.king.model.repository.ScoreRepository;
import com.king.utils.Utils;

/**
 * 
 **/
public class ScoreQueryTool {

	private static ScoreRepository repository;
	private static LoginService loginService;

	public ScoreQueryTool() {
	}

	/**
	 * Get user scores descending values
	 * 
	 * @param user
	 * @param level
	 * @return
	 */
	public static List<Integer> getScoreDesc(final User user, final int level) {
		return getScore(user, level, false);
	}

	/**
	 * Get user scores ascending values
	 * 
	 * @param user
	 * @param level
	 * @return
	 */
	public static List<Integer> getScoreAsc(final User user, final int level) {
		return getScore(user, level, true);

	}

	public static List<Integer> getScore(final User user, final int level, final boolean asc) {
		Score scoreObj = null;
		boolean valid = getLoginService().authenticated(user.getSession().getId());
		if (valid) {
			scoreObj = getScore(user, level);

			if (scoreObj != null) {
				if (asc) {
					scoreObj.getScores().sort((s1, s2) -> s1 - s2);
				} else {
					scoreObj.getScores().sort((s1, s2) -> s2 - s1);
				}
			}
		}
		return scoreObj.getScores();

	}

	/**
	 * Get the Scores for the given user and level in descending value
	 * 
	 * @param user
	 * @param level
	 * @return
	 */
	public static Score getScore(final User user, final int level) {
		Score scoreObj = null;
		boolean valid = getLoginService().authenticated(user.getSession().getId());
		if (valid) {
			if (user != null && level > 0) {
				scoreObj = getRepository().find(Utils.buildScoreKey(user, level));
			}
		}

		return scoreObj;
	}

	public static ScoreRepository getRepository() {
		if (null == repository) {
			repository = ScoreRepository.getInstance();
		}
		return repository;
	}

	public static LoginService getLoginService() {
		if (null == loginService) {
			loginService = SimpleLoginService.getInstance();
		}
		return loginService;
	}

}
