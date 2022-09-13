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

import com.poseidon.app.domain.Rating;
import com.poseidon.app.domain.dto.RatingDto;
import com.poseidon.app.exceptions.RatingServiceException;
import com.poseidon.app.services.RatingService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class RatingControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	RatingService ratingServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	static Rating mockFirstRating;
	static List<Rating> ratingListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ratingListMock = new ArrayList<>();
		mockFirstRating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		ratingListMock.add(mockFirstRating);
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_Ratings_Successful() throws Exception {

		// ARRANGE
		when(ratingServiceMock.findAllRatings()).thenReturn(ratingListMock);

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("ratings")); //
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_CreateRating_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("ratingDto")) //
				.andExpect(view().name("rating/add"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_CreateRating_Successful() throws Exception {

		// ARRANGE
		Rating ratingEntity = new Rating("Moodys", "SandP", "Fitch", 1);
		when(ratingServiceMock.convertDtoToEntity(any(RatingDto.class))).thenReturn(ratingEntity);
		when(ratingServiceMock.createRating(any(Rating.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/rating/validate") //
				.param("moodysRating", "Moodys") //
				.param("sandPRating", "SandP") //
				.param("fitchRating", "Fitch") //
				.param("orderNumber", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_UpdateRating_Successful() throws Exception {

		// ARRANGE
		Rating ratingEntity = new Rating("Moodys", "SandP", "Fitch", 1);
		when(ratingServiceMock.convertDtoToEntity(any(RatingDto.class))).thenReturn(ratingEntity);
		when(ratingServiceMock.updateRating(anyInt(), any(Rating.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/rating/update/{id}", "1") //
				.param("moodysRating", "Moodys") //
				.param("sandPRating", "SandP") //
				.param("fitchRating", "Fitch") //
				.param("orderNumber", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateRating_Successful() throws Exception {

		// ARRANGE
		Rating ratingEntity = new Rating("Moodys", "SandP", "Fitch", 1);
		RatingDto ratingDto = new RatingDto();
		ratingDto.setMoodysRating("Moodys");
		ratingDto.setSandPRating("SandP");
		ratingDto.setFitchRating("Fitch");
		ratingDto.setOrderNumber("1");

		when(ratingServiceMock.findRatingById(1)).thenReturn(ratingEntity);
		when(ratingServiceMock.convertEntityToDto(any(Rating.class))).thenReturn(ratingDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("ratingDto")) //
				.andExpect(view().name("rating/update"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateRating_Error() throws Exception {

		// ARRANGE
		when(ratingServiceMock.findRatingById(23)).thenThrow(RatingServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteRating_Successful() throws Exception {

		// ARRANGE
		when(ratingServiceMock.deleteRating(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/rating/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteRating_Error() throws Exception {

		// ARRANGE
		when(ratingServiceMock.deleteRating(1)).thenThrow(RatingServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/rating/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/rating/list"));
	}
}
