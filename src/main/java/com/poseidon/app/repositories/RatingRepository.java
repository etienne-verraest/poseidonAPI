package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidon.app.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
