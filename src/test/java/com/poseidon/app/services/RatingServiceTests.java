package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.poseidon.app.domain.Rating;
import com.poseidon.app.exceptions.RatingServiceException;
import com.poseidon.app.repositories.RatingRepository;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTests {

	@InjectMocks
	RatingService ratingService;

	@Mock
	RatingRepository ratingRepositoryMock;

	static Rating mockFirstRating;
	static Rating mockSecondRating;
	static List<Rating> ratingListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ratingListMock = new ArrayList<>();
		mockFirstRating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		mockSecondRating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 20);
		ratingListMock.add(mockFirstRating);
		ratingListMock.add(mockSecondRating);
	}

	@Test
	public void testFindAllRatings_ShouldReturn_List() {

		// ARRANGE
		when(ratingRepositoryMock.findAll()).thenReturn(ratingListMock);

		// ACT
		List<Rating> response = ratingService.findAllRatings();

		// ASSERT
		assertThat(response.get(0).getOrderNumber()).isEqualTo(10);
		assertThat(response).hasSize(2);

	}

	@Test
	public void testFindRatingById_ShouldReturn_FirstRating() throws RatingServiceException {

		// ARRANGE
		when(ratingRepositoryMock.findRatingById(1)).thenReturn(Optional.of(mockFirstRating));

		// ACT
		Rating response = ratingService.findRatingById(1);

		// ASSERT
		assertThat(response.getOrderNumber()).isEqualTo(10);

	}

	@Test
	public void testCreateRating_ShouldReturn_True() throws RatingServiceException {

		// ACT
		boolean response = ratingService.createRating(mockFirstRating);

		// ASSERT
		assertThat(response).isTrue();
		verify(ratingRepositoryMock, times(1)).save(mockFirstRating);

	}

	@Test
	public void testUpdateRating_ShouldReturn_True() throws RatingServiceException {

		// ARRANGE
		when(ratingRepositoryMock.findRatingById(2)).thenReturn(Optional.of(mockSecondRating));
		mockSecondRating.setOrderNumber(30);

		// ACT
		boolean response = ratingService.updateRating(2, mockSecondRating);

		assertThat(response).isTrue();
		verify(ratingRepositoryMock, times(1)).save(mockSecondRating);

	}

	@Test(expected = RatingServiceException.class)
	public void testUpdateRating_ShouldReturn_RatingServiceException() throws RatingServiceException {

		// ARRANGE
		when(ratingRepositoryMock.findRatingById(3)).thenReturn(Optional.empty());
		Rating newRating = new Rating();

		// ACT
		ratingService.updateRating(3, newRating);

		// ASSERT
		verify(ratingRepositoryMock, never()).save(newRating);
	}

	@Test
	public void testDeleteRating_ShouldReturn_True() throws RatingServiceException {

		// ARRANGE
		when(ratingRepositoryMock.findRatingById(2)).thenReturn(Optional.of(mockSecondRating));

		// ACT
		boolean response = ratingService.deleteRating(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(ratingRepositoryMock, times(1)).delete(mockSecondRating);
	}

}
