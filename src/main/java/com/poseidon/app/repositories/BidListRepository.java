package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.BidList;

@Repository
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
