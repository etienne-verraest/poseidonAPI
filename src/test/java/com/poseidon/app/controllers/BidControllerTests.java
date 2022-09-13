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

import com.poseidon.app.domain.Bid;
import com.poseidon.app.domain.dto.BidDto;
import com.poseidon.app.exceptions.BidServiceException;
import com.poseidon.app.services.BidService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class BidControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	BidService bidServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	static Bid mockFirstBid;
	static List<Bid> bidListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bidListMock = new ArrayList<>();
		mockFirstBid = new Bid("First Account", "Main", 10d);
		bidListMock.add(mockFirstBid);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_Bids_Successful() throws Exception {

		// ARRANGE
		when(bidServiceMock.findAllBids()).thenReturn(bidListMock);

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("bids")); //
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_CreateBid_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("bidDto")) //
				.andExpect(view().name("bidList/add"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_CreateBid_Successful() throws Exception {

		// ARRANGE
		Bid bidEntity = new Bid("Account", "type", 20d);
		when(bidServiceMock.convertDtoToEntity(any(BidDto.class))).thenReturn(bidEntity);
		when(bidServiceMock.createBid(any(Bid.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/bidList/validate") //
				.param("account", "Account") //
				.param("type", "Type") //
				.param("bidQuantity", "20")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/bidList/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_UpdateBid_Successful() throws Exception {

		// ARRANGE
		Bid bidEntity = new Bid("Account", "type", 20d);
		when(bidServiceMock.convertDtoToEntity(any(BidDto.class))).thenReturn(bidEntity);
		when(bidServiceMock.updateBid(anyInt(), any(Bid.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/bidList/update/{id}", "1") //
				.param("account", "Account") //
				.param("type", "Type") //
				.param("bidQuantity", "20")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/bidList/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateBid_Successful() throws Exception {

		// ARRANGE
		Bid bidEntity = new Bid("Account", "type", 20d);
		BidDto bidDto = new BidDto();
		bidDto.setAccount("Account");
		bidDto.setType("Type");
		bidDto.setBidQuantity("20");

		when(bidServiceMock.findBidById(1)).thenReturn(bidEntity);
		when(bidServiceMock.convertEntityToDto(any(Bid.class))).thenReturn(bidDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("bidDto")) //
				.andExpect(view().name("bidList/update"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateBid_Error() throws Exception {

		// ARRANGE
		when(bidServiceMock.findBidById(23)).thenThrow(BidServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/bidList/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteBid_Successful() throws Exception {

		// ARRANGE
		when(bidServiceMock.deleteBid(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/bidList/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteBid_Error() throws Exception {

		// ARRANGE
		when(bidServiceMock.deleteBid(1)).thenThrow(BidServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/bidList/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/bidList/list"));
	}

}
