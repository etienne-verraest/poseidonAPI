package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidon.app.domain.RuleName;


public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
