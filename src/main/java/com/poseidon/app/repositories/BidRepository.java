package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	Optional<Bid> findBidById(Integer id);
}
