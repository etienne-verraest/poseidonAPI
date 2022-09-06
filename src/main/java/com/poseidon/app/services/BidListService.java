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

	/**
	 * Get a list of every BidList
	 * @return									List<BidList> with existing BidList
	 */
	public List<BidList> findAllBidList() {
		return bidListRepository.findAll();
	}

	/**
	 * Find a BidList by its ID
	 *
	 * @param bidListId							The BidList ID to find
	 * @return									BidList if it exists, otherwise an error is thrown
	 * @throws BidListServiceException			Thrown if the BidList was not found
	 *
	 */
	public BidList findBidListById(Integer bidListId) throws BidListServiceException {
		Optional<BidList> bidList = bidListRepository.findByBidListId(bidListId);
		if (bidListId != null && bidList.isPresent()) {
			return bidList.get();
		}
		throw new BidListServiceException("Bid was not found with given ID");
	}

	/**
	 * Create a BidList
	 *
	 * @param bidListEntity						The BidList Entity to create
	 * @return									True if the creation was successful
	 * @throws BidListServiceException			Thrown if there was an error while creating the BidList
	 *
	 */
	public boolean createBidList(BidList bidListEntity) throws BidListServiceException {
		if (bidListEntity != null && !bidListRepository.findByBidListId(bidListEntity.getBidListId()).isPresent()) {
			bidListRepository.save(bidListEntity);
			log.info("[BID LIST SERVICE] Created new Bid List for account : '{}', quantity : '{}'",
					bidListEntity.getAccount(), bidListEntity.getBidQuantity());
			return true;
		}
		throw new BidListServiceException("There was an error while creating the Bid List");
	}

	/**
	 * Update an existing BidList
	 *
	 * @param bidListId							The BidList ID to update
	 * @param bidListEntityUpdated				The new fields given for update
	 * @return									True if the update was successful
	 * @throws BidListServiceException			Thrown if BidList with given ID is not found
	 */
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

	/**
	 * Delete a BidList
	 *
	 * @param bidListId							The BidList ID to delete
	 * @return									True if the deletion was successful
	 * @throws BidListServiceException			Thrown if BidList with given ID is not found
	 */
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
