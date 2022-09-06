package com.poseidon.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.BidList;
import com.poseidon.app.repositories.BidListRepository;

@Service
public class BidListService {

	@Autowired
	BidListRepository bidListRepository;

	public List<BidList> findAllBidList() {
		return bidListRepository.findAll();
	}

}
