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

import com.poseidon.app.domain.Trade;
import com.poseidon.app.domain.dto.TradeDto;
import com.poseidon.app.exceptions.TradeServiceException;
import com.poseidon.app.services.TradeService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	TradeService tradeServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	// **** Setting up IT values used for testing ****
	// I'll reuse values used in services tests
	static Trade mockFirstTrade;
	static List<Trade> tradeListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tradeListMock = new ArrayList<>();
		mockFirstTrade = new Trade("First Account", "First Type", 10d);
		tradeListMock.add(mockFirstTrade);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_Trades_Successful() throws Exception {

		// ARRANGE
		when(tradeServiceMock.findAllTrades()).thenReturn(tradeListMock);

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("trades")); //
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_CreateTrade_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("tradeDto")) //
				.andExpect(view().name("trade/add"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_CreateTrade_Successful() throws Exception {

		// ARRANGE
		Trade tradeEntity = new Trade("Account", "Type", 20d);

		when(tradeServiceMock.convertDtoToEntity(any(TradeDto.class))).thenReturn(tradeEntity);
		when(tradeServiceMock.createTrade(any(Trade.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/trade/validate") //
				.param("account", "Account") //
				.param("type", "Type") //
				.param("buyQuantity", "20")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/trade/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void post_UpdateTrade_Successful() throws Exception {

		// ARRANGE
		Trade tradeEntity = new Trade("Account", "Type", 20d);
		when(tradeServiceMock.convertDtoToEntity(any(TradeDto.class))).thenReturn(tradeEntity);
		when(tradeServiceMock.updateTrade(anyInt(), any(Trade.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/trade/update/{id}", "1") //
				.param("account", "Account") //
				.param("type", "Type") //
				.param("buyQuantity", "20")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/trade/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateTrade_Successful() throws Exception {

		// ARRANGE
		Trade tradeEntity = new Trade("Account", "Type", 20d);
		TradeDto tradeDto = new TradeDto();
		tradeDto.setAccount("Account");
		tradeDto.setType("Type");
		tradeDto.setBuyQuantity("20");

		when(tradeServiceMock.findTradeById(1)).thenReturn(tradeEntity);
		when(tradeServiceMock.convertEntityToDto(any(Trade.class))).thenReturn(tradeDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("tradeDto")) //
				.andExpect(view().name("trade/update"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_UpdateTrade_Error() throws Exception {

		// ARRANGE
		when(tradeServiceMock.findTradeById(23)).thenThrow(TradeServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/trade/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteTrade_Successful() throws Exception {

		// ARRANGE
		when(tradeServiceMock.deleteTrade(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/trade/list"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void get_DeleteTrade_Fail() throws Exception {

		// ARRANGE
		when(tradeServiceMock.deleteTrade(1)).thenThrow(TradeServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/trade/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/trade/list"));
	}
}
