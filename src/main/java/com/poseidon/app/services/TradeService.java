package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.Trade;
import com.poseidon.app.domain.dto.TradeDto;
import com.poseidon.app.exceptions.TradeServiceException;
import com.poseidon.app.repositories.TradeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradeService {

	@Autowired
	TradeRepository tradeRepository;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Get a list of every trades
	 * @return									List<Trade> with existing trades
	 */
	public List<Trade> findAllTrades() {
		return tradeRepository.findAll();
	}

	/**
	 * Find a Trade by its ID
	 *
	 * @param id								The Trade ID to find
	 * @return									Trade if it exists, otherwise an error is thrown
	 * @throws TradeServiceException	 		Thrown if the Trade was not found
	 */
	public Trade findTradeById(Integer id) throws TradeServiceException {
		Optional<Trade> trade = tradeRepository.findTradeById(id);
		if (id != null && trade.isPresent()) {
			return trade.get();
		}
		throw new TradeServiceException("Trade was not found with given ID");
	}

	/**
	 * Create a Trade
	 *
	 * @param tradeEntity						The Trade Entity to create
	 * @return									True if the creation was successful
	 * @throws TradeServiceException			Thrown if there was an error while creating the Trade
	 *
	 */
	public boolean createTrade(Trade tradeEntity) throws TradeServiceException {
		if (tradeEntity != null && !tradeRepository.findTradeById(tradeEntity.getId()).isPresent()) {
			tradeRepository.save(tradeEntity);
			log.info("[TRADE SERVICE] Created a new trade with id '{}'", tradeEntity.getId());
			return true;
		}
		throw new TradeServiceException("There was an error while creating the Trade");
	}

	/**
	 * Update an existing Trade
	 *
	 * @param id								The Trade ID to update
	 * @param tradeEntityUpdated				The new fields given for update
	 * @return									True if the update was successful
	 * @throws TradeServiceException			Thrown if Trade with given ID is not found
	 */
	public boolean updateTrade(Integer id, Trade tradeEntityUpdated) throws TradeServiceException {
		Optional<Trade> trade = tradeRepository.findTradeById(id);
		if (id != null && trade.isPresent()) {
			tradeEntityUpdated.setId(id);
			tradeRepository.save(tradeEntityUpdated);
			log.info("[TRADE SERVICE] Updated trade id '{}'", tradeEntityUpdated.getId());
			return true;
		}
		throw new TradeServiceException("Could not find rule with id : " + id);
	}

	/**
	 * Delete a Trade
	 *
	 * @param id								The Trade ID to delete
	 * @return									True if the deletion was successful
	 * @throws TradeServiceException			Thrown if Trade with given ID is not found
	 */
	public boolean deleteTrade(Integer id) throws TradeServiceException {
		Optional<Trade> trade = tradeRepository.findTradeById(id);
		if (id != null && trade.isPresent()) {
			tradeRepository.delete(trade.get());
			log.info("[TRADE SERVICE] Deleted trade id '{}'", id);
			return true;
		}
		throw new TradeServiceException("Could not find Trade with id : " + id);
	}

	public Trade convertDtoToEntity(TradeDto tradeDto) {
		return modelMapper.map(tradeDto, Trade.class);
	}

	public TradeDto convertEntityToDto(Trade tradeEntity) {
		return modelMapper.map(tradeEntity, TradeDto.class);
	}

}
