package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.poseidon.app.domain.Trade;
import com.poseidon.app.exceptions.TradeServiceException;
import com.poseidon.app.repositories.TradeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceTests {

	@InjectMocks
	TradeService tradeService;

	@Mock
	TradeRepository tradeRepositoryMock;

	static Trade mockFirstTrade;
	static Trade mockSecondTrade;
	static List<Trade> tradeListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tradeListMock = new ArrayList<>();
		mockFirstTrade = new Trade("First Account", "First Type");
		mockSecondTrade = new Trade("Second Account", "Second Type");
		tradeListMock.add(mockFirstTrade);
		tradeListMock.add(mockSecondTrade);
	}

	@Test
	public void testFindAllTrades_ShouldReturn_List() {

		// ARRANGE
		when(tradeRepositoryMock.findAll()).thenReturn(tradeListMock);

		// ACT
		List<Trade> response = tradeService.findAllTrades();

		// ASSERT
		assertThat(response).hasSize(2);
	}

	@Test
	public void testFindTradeById_ShouldReturn_FirstTrade() throws TradeServiceException {

		// ARRANGE
		when(tradeRepositoryMock.findTradeById(1)).thenReturn(Optional.of(mockFirstTrade));

		// ACT
		Trade response = tradeService.findTradeById(1);

		// ASSERT
		assertThat(response.getAccount()).isEqualTo("First Account");

	}

	@Test
	public void testCreateTrade_ShouldReturn_True() throws TradeServiceException {

		// ACT
		boolean response = tradeService.createTrade(mockFirstTrade);

		// ASSERT
		assertThat(response).isTrue();
		verify(tradeRepositoryMock, times(1)).save(mockFirstTrade);
	}

	@Test
	public void testUpdateTrade_ShouldReturn_True() throws TradeServiceException {

		// ARRANGE
		when(tradeRepositoryMock.findTradeById(2)).thenReturn(Optional.of(mockSecondTrade));
		mockSecondTrade.setType("Second Type Updated");

		// ACT
		boolean response = tradeService.updateTrade(2, mockSecondTrade);

		// ASSER
		assertThat(response).isTrue();
		verify(tradeRepositoryMock, times(1)).save(mockSecondTrade);
	}

	@Test
	public void testDeleteTrade_ShouldReturn_True() throws TradeServiceException {

		// ARRANGE
		when(tradeRepositoryMock.findTradeById(2)).thenReturn(Optional.of(mockSecondTrade));

		// ACT
		boolean response = tradeService.deleteTrade(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(tradeRepositoryMock, times(1)).delete(mockSecondTrade);
	}

}
