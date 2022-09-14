package com.poseidon.app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.poseidon.app.domain.User;
import com.poseidon.app.domain.dto.UserDto;
import com.poseidon.app.exceptions.UserServiceException;
import com.poseidon.app.services.UserService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	UserService userServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	static User mockFirstUser;
	static List<User> userMockList;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userMockList = new ArrayList<>();
		mockFirstUser = new User(1, "tom.powell", "Passw0rd-", "Tom Powell", "USER");
		userMockList.add(mockFirstUser);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_Users_Successful() throws Exception {

		// ARRANGE
		when(userServiceMock.findAllUsers()).thenReturn(userMockList);

		// ACT AND ASSERT
		mockMvc.perform(get("/user/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("users")); //
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_CreateUser_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/user/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("userDto")) //
				.andExpect(view().name("user/add"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_CreateUser_Successful() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "Passw0rd-", "David Waters", "USER");
		when(userServiceMock.convertDtoToEntity(any(UserDto.class))).thenReturn(userEntity);
		when(userServiceMock.createUser(any(User.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/user/validate") //
				.param("fullname", "Account") //
				.param("username", "Type") //
				.param("password", "Passw0rd-") //
				.param("role", "USER")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/user/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_UpdateUser_Successful() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "password", "David Waters", "USER");
		when(userServiceMock.convertDtoToEntity(any(UserDto.class))).thenReturn(userEntity);
		when(userServiceMock.updateUser(anyInt(), any(User.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/user/update/{id}", "1") //
				.param("fullname", "Account") //
				.param("username", "Type") //
				.param("password", "Passw0rd-") //
				.param("role", "USER")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/user/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateUser_Successful() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "Passw0rd-", "David Waters", "USER");
		UserDto userDto = new UserDto();
		userDto.setFullname("David Waters");
		userDto.setUsername("david.waters");
		userDto.setPassword("Passw0rd-");
		userDto.setRole("USER");

		when(userServiceMock.findUserById(1)).thenReturn(userEntity);
		when(userServiceMock.convertEntityToDto(any(User.class))).thenReturn(userDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/user/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("userDto")) //
				.andExpect(view().name("user/update"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateUser_Error() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "Passw0rd-", "David Waters", "USER");
		when(userServiceMock.findUserById(1)).thenReturn(userEntity);
		when(userServiceMock.findUserById(23)).thenThrow(UserServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/user/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/user/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteUser_Successful() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "Passw0rd-", "David Waters", "USER");
		when(userServiceMock.findUserById(1)).thenReturn(userEntity);
		when(userServiceMock.deleteUser(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/user/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/user/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteUser_Error() throws Exception {

		// ARRANGE
		User userEntity = new User(1, "david.waters", "Passw0rd-", "David Waters", "USER");
		when(userServiceMock.findUserById(1)).thenReturn(userEntity);
		when(userServiceMock.deleteUser(1)).thenThrow(UserServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/user/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/user/list"));
	}

}
