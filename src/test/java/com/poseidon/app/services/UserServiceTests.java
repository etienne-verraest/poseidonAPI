package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.security.core.userdetails.UserDetails;

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
	public void testLoadUserByUserName_ShouldReturn_User() {

		// ARRANGE
		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(mockFirstUser));

		// ACT
		UserDetails response = userService.loadUserByUsername("tom.powell");

		// ASSERT
		assertThat(response.getUsername()).isEqualTo("tom.powell");

	}

	@Test
	public void testFindAllUsers_ShouldReturn_TwoUsers() {

		// ARRANGE
		when(userRepositoryMock.findAll()).thenReturn(userMockList);

		// ACT
		List<User> list = userService.findAllUsers();

		// ASSERT
		assertThat(list.get(0).getFullname()).isEqualTo("Tom Powell");
		assertThat(list.get(1).getFullname()).isEqualTo("David Waters");
		assertThat(list).hasSize(2);
	}

	@Test
	public void testFindUserById_ShouldReturn_FirstUser() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(1)).thenReturn(Optional.of(mockFirstUser));

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
		verify(userRepositoryMock, times(1)).save(mockSecondUser);
	}

	@Test(expected = UserServiceException.class)
	public void testCreateUser_ShouldReturn_UsernameTaken() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(mockSecondUser));

		// ACT
		userService.createUser(mockSecondUser);

		// ASSERT
		verify(userRepositoryMock, never()).save(mockSecondUser);
	}

	@Test
	public void testUpdateUser_ShouldReturn_True() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(1)).thenReturn(Optional.of(mockFirstUser));
		mockFirstUser.setRole("ADMIN");

		// ACT
		boolean response = userService.updateUser(1, mockFirstUser);

		// ASSERT
		assertThat(response).isTrue();
		verify(userRepositoryMock, times(1)).save(mockFirstUser);
	}

	@Test(expected = UserServiceException.class)
	public void testUpdateUser_ShouldThrow_Exception() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(3)).thenReturn(Optional.empty());

		// ACT
		userService.updateUser(3, new User());

		// ASSERT
		verify(userRepositoryMock, never()).save(new User());
	}

	@Test
	public void testDeleteUser_ShouldReturn_True() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(2)).thenReturn(Optional.of(mockSecondUser));

		// ACT
		boolean response = userService.deleteUser(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(userRepositoryMock, times(1)).delete(mockSecondUser);
	}

	@Test(expected = UserServiceException.class)
	public void testDeleteUser_ShouldThrow_Exception() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.findById(3)).thenReturn(Optional.empty());

		// ACT
		userService.deleteUser(3);

		// ASSERT
		verify(userRepositoryMock, never()).delete(new User());
	}

}
