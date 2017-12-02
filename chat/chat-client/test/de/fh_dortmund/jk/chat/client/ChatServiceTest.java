package de.fh_dortmund.jk.chat.client;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.jk.chat.beans.exception.AlreadyLoggedInException;
import de.fh_dortmund.jk.chat.beans.exception.NotAuthenticatedException;
import de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryRemote;

public class ChatServiceTest {

	private static UserRepositoryRemote users;
	private ServiceHandlerImpl chatService;

	@BeforeClass
	public static void initClass() {
		try {
			Context ctx = new InitialContext();
			users = (UserRepositoryRemote) ctx.lookup(
					"java:global/chat-ear/chat-ejb/UserRepositoryBean!de.fh_dortmund.jk.chat.beans.interfaces.UserRepositoryRemote");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void init() throws Exception {
		users.deleteAll();

		chatService = new ServiceHandlerImpl();

		chatService.register("test", "test");
	}

	@After
	public void close() {
		try {
			chatService.disconnect();
		} catch (Exception e) {
			// ignored
		}
	}

	@Test
	public void testUserName() throws Exception {
		chatService.login("test", "test");

		assertEquals("test", chatService.getUserName());
	}

	@Test(expected = RuntimeException.class)
	public void testUserNameWhenNotLoggedIn() throws Exception {
		chatService.getUserName();
	}

	@Test
	public void testLogin() throws Exception {
		chatService.login("test", "test");

		assertThat(chatService.getOnlineUsers(), hasItem("test"));
	}

	@Test(expected = NotAuthenticatedException.class)
	public void testLoginWithWrongPassword() throws Exception {
		chatService.login("test", "test2");
	}

	@Test
	public void testRegister() throws Exception {
		chatService.register("test2", "test2");

		assertEquals(2, chatService.getNumberOfRegisteredUsers());

		chatService.login("test2", "test2");
	}

	@Test
	public void testNumberOfOnlineUsers() throws Exception {
		assertEquals(0, chatService.getNumberOfOnlineUsers());

		chatService.login("test", "test");

		assertEquals(1, chatService.getNumberOfOnlineUsers());
	}

	@Test
	public void testNumberOfRegisteredUsers() throws Exception {
		assertEquals(1, chatService.getNumberOfRegisteredUsers());

		chatService.register("test2", "test2");

		assertEquals(2, chatService.getNumberOfRegisteredUsers());
	}

	@Test
	public void testOnlineUsers() throws Exception {
		chatService.login("test", "test");

		assertThat(chatService.getOnlineUsers(), hasItem("test"));
	}

	@Test(expected = AlreadyLoggedInException.class)
	public void testChangePassword() throws Exception {
		chatService.login("test", "test");
		chatService.changePassword("test", "test2");

		chatService.login("test", "test2");
	}

	@Test(expected = NotAuthenticatedException.class)
	public void testChangePasswordWithWrongPassword() throws Exception {
		chatService.login("test", "test");
		chatService.changePassword("wrong", "test2");
	}

	@Test
	public void testLogout() throws Exception {
		chatService.login("test", "test");

		assertThat(chatService.getOnlineUsers(), hasItem("test"));
		chatService.logout();

		try {
			chatService.getUserName();
		} catch (Exception e) {
			return;
		}

		fail();
	}

	@Test(expected = NotAuthenticatedException.class)
	public void testLogoutWhenNotLoggedIn() throws Exception {
		chatService.logout();
	}

	@Test
	public void testDisconnect() throws Exception {
		chatService.disconnect();

		try {
			chatService.getUserName();
		} catch (Exception e) {
			return;
		}

		fail();
	}

	@Test
	public void testDelete() throws Exception {
		chatService.login("test", "test");
		chatService.delete("test");

		try {
			chatService.getUserName();
		} catch (Exception e) {
			return;
		}

		fail();
	}

	@Test(expected = NotAuthenticatedException.class)
	public void testDeleteWithWrongPassword() throws Exception {
		chatService.login("test", "test");
		chatService.delete("wrong");
	}

	@Test
	public void testStatistics() {
		List<CommonStatistic> stats = chatService.getStatistics();
		
		assertThat(stats, not(empty()));
	}

	@Test(expected = RuntimeException.class)
	public void testUserStatisticWithoutLogin() throws Exception {
		chatService.getUserStatistic();
	}

	@Test
	public void testUserStatistic() throws Exception {
		chatService.login("test", "test");
		UserStatistic stats = chatService.getUserStatistic();
		
		assertThat(stats.getLogins(), greaterThanOrEqualTo(1));
	}
}
