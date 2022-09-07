package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.Bid;
import com.poseidon.app.exceptions.BidServiceException;
import com.poseidon.app.repositories.BidRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidService {

	@Autowired
	BidRepository bidRepository;

	/**
	 * Get a list of every Bid
	 * @return									List<Bid> with existing Bid
	 */
	public List<Bid> findAllBids() {
		return bidRepository.findAll();
	}

	/**
	 * Find a Bid by its ID
	 *
	 * @param id								The Bid ID to find
	 * @return									Bid if it exists, otherwise an error is thrown
	 * @throws BidServiceException				Thrown if the Bid was not found
	 *
	 */
	public Bid findBidById(Integer id) throws BidServiceException {
		Optional<Bid> bid = bidRepository.findBidById(id);
		if (id != null && bid.isPresent()) {
			return bid.get();
		}
		throw new BidServiceException("Bid was not found with given ID");
	}

	/**
	 * Create a Bid
	 *
	 * @param bidEntity							The Bid Entity to create
	 * @return									True if the creation was successful
	 * @throws BidServiceException				Thrown if there was an error while creating the Bid
	 *
	 */
	public boolean createBid(Bid bidEntity) throws BidServiceException {
		if (bidEntity != null && !bidRepository.findBidById(bidEntity.getId()).isPresent()) {
			bidRepository.save(bidEntity);
			log.info("[BID SERVICE] Created new Bid List for account : '{}', quantity : '{}'", bidEntity.getAccount(),
					bidEntity.getBidQuantity());
			return true;
		}
		throw new BidServiceException("There was an error while creating the Bid List");
	}

	/**
	 * Update an existing Bid
	 *
	 * @param id								The Bid ID to update
	 * @param bidEntityUpdated					The new fields given for update
	 * @return									True if the update was successful
	 * @throws BidServiceException				Thrown if Bid with given ID is not found
	 */
	public boolean updateBid(Integer id, Bid bidEntityUpdated) throws BidServiceException {
		Optional<Bid> bidList = bidRepository.findBidById(id);
		if (id != null && bidList.isPresent()) {

			bidEntityUpdated.setId(id);
			bidRepository.save(bidEntityUpdated);

			log.info("[BID SERVICE] Updated account '{}' with id '{}'", bidEntityUpdated.getAccount(), id);
			return true;
		}
		throw new BidServiceException("Could not find bid list with id : " + id);
	}

	/**
	 * Delete a Bid
	 *
	 * @param id								The Bid ID to delete
	 * @return									True if the deletion was successful
	 * @throws BidServiceException				Thrown if Bid with given ID is not found
	 */
	public boolean deleteBid(Integer id) throws BidServiceException {
		Optional<Bid> bid = bidRepository.findBidById(id);
		if (id != null && bid.isPresent()) {
			bidRepository.delete(bid.get());
			log.info("[BID SERVICE] Deleted bid list with id '{}'", id);
			return true;
		}
		throw new BidServiceException("Could not find bid list with id : " + id);
	}

}
