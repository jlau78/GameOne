package com.king.model.profile;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ScoreTest extends TestCase {

	Score testComponent = new Score(1, 101);

	@Before
	public void setUp() throws Exception {
		testComponent.setMaxScoresKept(5);
	}

	@Test
	public void testAddScore() {

		testComponent.addScore(1, 1124);
		testComponent.addScore(1, 1524);
		testComponent.addScore(1, 1824);
		testComponent.addScore(1, 4124);
		testComponent.addScore(1, 124);
		testComponent.addScore(1, 1824);
		testComponent.addScore(1, 3134);
		testComponent.addScore(1, 24);
		testComponent.addScore(1, 112);
		testComponent.addScore(1, 11);
		testComponent.addScore(1, 114);

		System.out.println(testComponent.getScores());

		testComponent.getScores().sort((o1, o2) -> o1 - o2);
		Assert.assertTrue(testComponent.getScores().get(0) > 500);

		Assert.assertEquals(5, testComponent.getScores().size());

	}

}
