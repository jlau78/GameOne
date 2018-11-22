package com.king.server.service;

import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.king.model.profile.User;

import junit.framework.Assert;
import junit.framework.TestCase;

public class LoginTest extends TestCase {

	protected SimpleLoginService testComponent = SimpleLoginService.getInstance();

	@Mock
	protected User user;

	static final String SESSION_ID_1 = "John|pass101010";

	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(user.getName()).thenReturn("John");
		when(user.getPwd()).thenReturn("pass");

	}

	public void testLoginSuccess() {

		User user = testComponent.login(SESSION_ID_1);

		Assert.assertNotNull(user);

		testComponent.printSessions();

	}

	public void testLoginWithNull() {

		User user = testComponent.login(null);

		Assert.assertNull(user);

	}

	public void testAuthenticated() {

		String cred = "NewJohn|Pass90900";
		User user = testComponent.login(cred);

		boolean success = testComponent.authenticated(user.getSession().getId());
		Assert.assertTrue(success);

	}

}
