package com.king.client;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.king.model.profile.User;
import com.king.server.service.LoginService;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GameTest extends TestCase {

	@Spy
	Game testComponent = GameOne.getInstance();

	@Mock
	LoginService loginService;

	User user = new User();

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		((GameOne) testComponent).setLoginService(loginService);

		user.setName("John");
		user.setCurrentLevel(4);
		user.setCurrentScore(1200);

		when(loginService.authenticated(Mockito.anyString())).thenReturn(true);

	}

	public void testStart() {

		User user1 = new User();
		when(loginService.login(Mockito.any())).thenReturn(user1);
		user1 = testComponent.start("John", "password");

		Assert.assertNotNull(user1);
	}

	public void testPlay() throws InterruptedException {

		Runnable GameRunner = () -> testComponent.play(user);
		Thread t1 = new Thread(GameRunner);
		t1.start();

		Thread.sleep(1000);

		testComponent.stop(user);

		verify(testComponent, times(1)).sendScore(user, user.getCurrentLevel(), user.getCurrentScore());

	}

}
