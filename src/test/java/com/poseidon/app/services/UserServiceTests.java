package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.poseidon.app.domain.User;
import com.poseidon.app.exceptions.UserServiceException;
import com.poseidon.app.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepositoryMock;

	static User mockFirstUser;
	static User mockSecondUser;
	static List<User> userMockList;

	// Creating two mock users which will be used as test datas
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userMockList = new ArrayList<>();
		mockFirstUser = new User(1, "tom.powell", "password", "Tom Powell", "USER");
		mockSecondUser = new User(2, "david.waters", "password", "David Waters", "ADMIN");
		userMockList.add(mockFirstUser);
		userMockList.add(mockSecondUser);
	}

	@Test
	public void testFindUserById_ShouldReturn_FirstUser() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(1)).thenReturn(Optional.of(userMockList.get(0)));

		// ACT
		User response = userService.findUserById(1);

		// ASSERT
		assertThat(response.getUsername()).isEqualTo(mockFirstUser.getUsername());
		assertThat(response.getRole()).isEqualTo(mockFirstUser.getRole());
	}

	@Test(expected = RuntimeException.class)
	public void testFindUserById_ShouldThrow_Exception() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(anyInt())).thenThrow(new RuntimeException(""));

		// ACT
		userService.findUserById(3);
	}

	@Test
	public void testCreateUser_ShouldReturn_True() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.empty());

		// ACT
		boolean response = userService.createUser(mockSecondUser);

		// ASSERT
		assertThat(response).isTrue();
	}

	@Test(expected = UserServiceException.class)
	public void testCreateUser_ShouldReturn_UsernameTaken() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(mockSecondUser));

		// ACT
		userService.createUser(mockSecondUser);
	}

}
