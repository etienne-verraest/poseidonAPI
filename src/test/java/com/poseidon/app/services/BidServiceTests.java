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
	BidService bidService;

	@Mock
	BidRepository bidRepositoryMock;

	static Bid mockFirstBid;
	static Bid mockSecondBid;
	static List<Bid> bidListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bidListMock = new ArrayList<>();
		mockFirstBid = new Bid("First Account", "Main", 10d);
		mockSecondBid = new Bid("Second Account", "Secondary", 2d);
		bidListMock.add(mockFirstBid);
		bidListMock.add(mockSecondBid);
	}

	@Test
	public void test1_FindAllBidList_ShouldReturn_List() {

		// ARRANGE
		when(bidRepositoryMock.findAll()).thenReturn(bidListMock);

		// ACT
		List<Bid> response = bidService.findAllBids();

		// ASSERT
		assertThat(response.get(0).getBidQuantity()).isEqualTo(10d);
		assertThat(response.get(1).getBidQuantity()).isEqualTo(2d);
		assertThat(response).hasSize(2);
	}

	@Test
	public void testFindBidListById_ShouldReturn_FirstBidList() throws BidServiceException {

		// ARRANGE
		when(bidRepositoryMock.findBidById(1)).thenReturn(Optional.of(mockFirstBid));

		// ACT
		Bid response = bidService.findBidById(1);

		// ASSERT
		assertThat(response.getAccount()).isEqualTo("First Account");
		assertThat(response.getType()).isEqualTo("Main");
	}

	@Test
	public void testCreateBidList_ShouldReturn_True() throws BidServiceException {

		// ACT
		boolean response = bidService.createBid(mockFirstBid);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidRepositoryMock, times(1)).save(mockFirstBid);

	}

	@Test
	public void testUpdateBidList_ShouldReturn_True() throws BidServiceException {

		// ARRANGE
		when(bidRepositoryMock.findBidById(1)).thenReturn(Optional.of(mockFirstBid));
		mockFirstBid.setBidQuantity(4d);

		// ACT
		boolean response = bidService.updateBid(1, mockFirstBid);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidRepositoryMock, times(1)).save(mockFirstBid);

	}

	@Test
	public void testDeleteBidList_ShouldReturn_True() throws BidServiceException {

		// ARRANGE
		when(bidRepositoryMock.findBidById(2)).thenReturn(Optional.of(mockSecondBid));

		// ACT
		boolean response = bidService.deleteBid(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidRepositoryMock, times(1)).delete(mockSecondBid);
	}

}
