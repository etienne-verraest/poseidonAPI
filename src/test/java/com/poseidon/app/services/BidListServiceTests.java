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

import com.poseidon.app.domain.BidList;
import com.poseidon.app.exceptions.BidListServiceException;
import com.poseidon.app.repositories.BidListRepository;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BidListServiceTests {

	@InjectMocks
	BidListService bidListService;

	@Mock
	BidListRepository bidListRepositoryMock;

	static BidList mockFirstBidList;
	static BidList mockSecondBidList;
	static List<BidList> bidListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bidListMock = new ArrayList<>();
		mockFirstBidList = new BidList("First Account", "Main", 10d);
		mockSecondBidList = new BidList("Second Account", "Secondary", 2d);
		bidListMock.add(mockFirstBidList);
		bidListMock.add(mockSecondBidList);
	}

	@Test
	public void test1_FindAllBidList_ShouldReturn_List() {

		// ARRANGE
		when(bidListRepositoryMock.findAll()).thenReturn(bidListMock);

		// ACT
		List<BidList> response = bidListService.findAllBidList();

		// ASSERT
		assertThat(response.get(0).getBidQuantity()).isEqualTo(10d);
		assertThat(response.get(1).getBidQuantity()).isEqualTo(2d);
		assertThat(response).hasSize(2);
	}

	@Test
	public void testFindBidListById_ShouldReturn_FirstBidList() throws BidListServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findByBidListId(1)).thenReturn(Optional.of(mockFirstBidList));

		// ACT
		BidList response = bidListService.findBidListById(1);

		// ASSERT
		assertThat(response.getAccount()).isEqualTo("First Account");
		assertThat(response.getType()).isEqualTo("Main");
	}

	@Test
	public void testCreateBidList_ShouldReturn_True() throws BidListServiceException {

		// ACT
		boolean response = bidListService.createBidList(mockFirstBidList);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).save(mockFirstBidList);

	}

	@Test
	public void testUpdateBidList_ShouldReturn_True() throws BidListServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findByBidListId(1)).thenReturn(Optional.of(mockFirstBidList));
		mockFirstBidList.setBidQuantity(4d);

		// ACT
		boolean response = bidListService.updateBidList(1, mockFirstBidList);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).save(mockFirstBidList);

	}

	@Test
	public void testDeleteBidList_ShouldReturn_True() throws BidListServiceException {

		// ARRANGE
		when(bidListRepositoryMock.findByBidListId(2)).thenReturn(Optional.of(mockSecondBidList));

		// ACT
		boolean response = bidListService.deleteBidList(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(bidListRepositoryMock, times(1)).delete(mockSecondBidList);
	}

}
