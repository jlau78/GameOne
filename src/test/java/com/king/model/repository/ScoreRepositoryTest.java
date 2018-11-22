package com.king.model.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.king.model.profile.Score;

import junit.framework.TestCase;

public class ScoreRepositoryTest extends TestCase {

	ScoreRepository testComponent = ScoreRepository.getInstance();

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testFindNullKey() {
		Score s = testComponent.find(null);
		Assert.assertNull(s);
	}

	public void testRemoveNullKey() {
		boolean s = testComponent.remove(null);

		Assert.assertFalse(s);
	}

}
