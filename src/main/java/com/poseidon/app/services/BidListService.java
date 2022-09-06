package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.BidList;
import com.poseidon.app.exceptions.BidListServiceException;
import com.poseidon.app.repositories.BidListRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidListService {

	@Autowired
	BidListRepository bidListRepository;

	public List<BidList> findAllBidList() {
		return bidListRepository.findAll();
	}

	public BidList findBidListById(Integer bidListId) throws BidListServiceException {
		Optional<BidList> bidList = bidListRepository.findByBidListId(bidListId);
		if (bidListId != null && bidList.isPresent()) {
			return bidList.get();
		}
		throw new BidListServiceException("Bid was not found with given ID");
	}

	public boolean createBidList(BidList bidListEntity) throws BidListServiceException {
		if (bidListEntity != null && !bidListRepository.findByBidListId(bidListEntity.getBidListId()).isPresent()) {
			bidListRepository.save(bidListEntity);
			log.info("[BID LIST SERVICE] Created new Bid List for account : '{}', quantity : '{}'",
					bidListEntity.getAccount(), bidListEntity.getBidQuantity());
			return true;
		}
		throw new BidListServiceException("There was an error while creating the Bid List");
	}

	public boolean updateBidList(Integer bidListId, BidList bidListEntityUpdated) throws BidListServiceException {
		Optional<BidList> bidList = bidListRepository.findByBidListId(bidListId);
		if (bidListId != null && bidList.isPresent()) {

			bidListEntityUpdated.setBidListId(bidListId);
			bidListRepository.save(bidListEntityUpdated);

			log.info("[BID LIST SERVICE] Updated account '{}' with id '{}'", bidListEntityUpdated.getAccount(),
					bidListId);
			return true;
		}
		throw new BidListServiceException("Could not find bid list with id : " + bidListId);
	}

	public boolean deleteBidList(Integer bidListId) throws BidListServiceException {
		Optional<BidList> bidList = bidListRepository.findByBidListId(bidListId);
		if (bidListId != null && bidList.isPresent()) {
			bidListRepository.delete(bidList.get());
			log.info("[BID LIST SERVICE] Deleted bid list with id '{}'", bidListId);
			return true;
		}

		throw new BidListServiceException("Could not find bid list with id : " + bidListId);
	}

}
