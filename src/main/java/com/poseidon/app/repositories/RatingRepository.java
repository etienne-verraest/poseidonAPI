package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

	Optional<Rating> findRatingById(Integer id);
}
