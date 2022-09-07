package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {

	Optional<Rule> findRuleById(Integer id);

}
