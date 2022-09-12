package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.Rating;
import com.poseidon.app.domain.dto.RatingDto;
import com.poseidon.app.exceptions.RatingServiceException;
import com.poseidon.app.repositories.RatingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RatingService {

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Get a list of every ratings
	 * @return									List<Rating> with existing ratings
	 */
	public List<Rating> findAllRatings() {
		return ratingRepository.findAll();
	}

	/**
	 * Find a Rating by its ID
	 *
	 * @param id								The Rating ID to find
	 * @return									Rating if it exists, otherwise an error is thrown
	 * @throws RatingServiceException	 		Thrown if the Rating was not found
	 */
	public Rating findRatingById(Integer id) throws RatingServiceException {
		Optional<Rating> rating = ratingRepository.findRatingById(id);
		if (id != null && rating.isPresent()) {
			return rating.get();
		}
		throw new RatingServiceException("Rating was not found with given ID");
	}

	/**
	 * Create a Rating
	 *
	 * @param ratingEntity						The Rating Entity to create
	 * @return									True if the creation was successful
	 * @throws RatingServiceException			Thrown if there was an error while creating the Rating
	 *
	 */
	public boolean createRating(Rating ratingEntity) throws RatingServiceException {
		if (ratingEntity != null && !ratingRepository.findRatingById(ratingEntity.getId()).isPresent()) {
			ratingRepository.save(ratingEntity);
			log.info("[RATING SERVICE] Created a new rating with id '{}' for order number '{}'", ratingEntity.getId(),
					ratingEntity.getOrderNumber());
			return true;
		}
		throw new RatingServiceException("There was an error while creating the rating");
	}

	/**
	 * Update an existing Rating
	 *
	 * @param id								The Rating ID to update
	 * @param ratingEntityUpdated				The new fields given for update
	 * @return									True if the update was successful
	 * @throws RatingServiceException			Thrown if Rating with given ID is not found
	 */
	public boolean updateRating(Integer id, Rating ratingEntityUpdated) throws RatingServiceException {
		Optional<Rating> rating = ratingRepository.findRatingById(id);
		if (id != null && rating.isPresent()) {
			ratingEntityUpdated.setId(id);
			ratingRepository.save(ratingEntityUpdated);

			log.info("[RATING SERVICE] Updated rating's id '{}' for order number '{}'", ratingEntityUpdated.getId(),
					ratingEntityUpdated.getOrderNumber());
			return true;
		}
		throw new RatingServiceException("Could not find rating with id : " + id);
	}

	/**
	 * Delete a Rating
	 *
	 * @param id								The Rating ID to delete
	 * @return									True if the deletion was successful
	 * @throws RatingServiceException			Thrown if Rating with given ID is not found
	 */
	public boolean deleteRating(Integer id) throws RatingServiceException {
		Optional<Rating> rating = ratingRepository.findRatingById(id);
		if (id != null && rating.isPresent()) {
			ratingRepository.delete(rating.get());
			log.info("[RATING SERVICE] Deleted rating's id '{}' for order number '{}'", id,
					rating.get().getOrderNumber());
			return true;
		}
		throw new RatingServiceException("Could not find rating with id : " + id);
	}

	public Rating convertDtoToEntity(RatingDto ratingDto) {
		return modelMapper.map(ratingDto, Rating.class);
	}

	public RatingDto convertEntityToDto(Rating ratingEntity) {
		return modelMapper.map(ratingEntity, RatingDto.class);
	}

}
