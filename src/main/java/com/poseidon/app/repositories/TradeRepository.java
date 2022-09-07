package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

	Optional<Trade> findTradeById(Integer id);
}
