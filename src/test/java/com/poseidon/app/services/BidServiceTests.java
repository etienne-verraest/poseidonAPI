package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.poseidon.app.domain.Bid;
import com.poseidon.app.exceptions.BidServiceException;
import com.poseidon.app.repositories.BidRepository;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BidServiceTests {

	@InjectMocks
	BidService bidListService;

	@Mock
	BidRepository bidListRepositoryMock;

	static Bid mockFirstBidList;
	static Bid mockSecondBidList;
	static List<Bid> bidListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bidListMock = new ArrayList<>();
		mockFirstBidList = new Bid("First Account", "Main", 10d);
		mockSecondBidList = new Bid("Second Account", "Secondary", 2d);
		bidListMock.add(mockFirstBidList);
		bidListMock.add(mockSecondBidList);
	}

	@Test
	public void test1_FindAllBidList_ShouldReturn_List() {

		// ARRANGE
		when(bidListRepositoryMock.findAll()).thenReturn(bidListMock);

		// ACT
		List<Bid> response = bidListService.findAllBids();

		// ASSERT
		assertThat(response.get(0).getBidQuantity()).isEqualTo(10d);
		assertThat(response.get(1).getBidQuantity()).isEqualTo(2d);
		assertThat(response).hasSize(2);
	}

	@Test
	public void testFindBidListById_ShouldReturn_FirstBidList() throws BidServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findBidById(1)).thenReturn(Optional.of(mockFirstBidList));

		// ACT
		Bid response = bidListService.findBidById(1);

		// ASSERT
		assertThat(response.getAccount()).isEqualTo("First Account");
		assertThat(response.getType()).isEqualTo("Main");
	}

	@Test
	public void testCreateBidList_ShouldReturn_True() throws BidServiceException {

		// ACT
		boolean response = bidListService.createBid(mockFirstBidList);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).save(mockFirstBidList);

	}

	@Test
	public void testUpdateBidList_ShouldReturn_True() throws BidServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findBidById(1)).thenReturn(Optional.of(mockFirstBidList));
		mockFirstBidList.setBidQuantity(4d);

		// ACT
		boolean response = bidListService.updateBid(1, mockFirstBidList);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).save(mockFirstBidList);

	}

	@Test
	public void testDeleteBidList_ShouldReturn_True() throws BidServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findBidById(2)).thenReturn(Optional.of(mockSecondBidList));

		// ACT
		boolean response = bidListService.deleteBid(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).delete(mockSecondBidList);
	}

}
