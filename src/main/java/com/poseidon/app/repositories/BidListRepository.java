package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.BidList;

@Repository
public interface BidListRepository extends JpaRepository<BidList, Integer> {

	Optional<BidList> findByBidListId(Integer bidListId);
}
