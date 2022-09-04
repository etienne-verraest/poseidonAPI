package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidon.app.domain.Trade;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
