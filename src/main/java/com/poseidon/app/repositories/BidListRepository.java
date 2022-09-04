package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidon.app.domain.BidList;


public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
