package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.exceptions.RuleNameServiceException;
import com.poseidon.app.repositories.RuleNameRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RuleNameService {

	@Autowired
	RuleNameRepository ruleNameRepository;

	/**
	 * Get a list of every ruless
	 * @return									List<RuleName> with existing rules
	 */
	public List<RuleName> findAllRuleNames() {
		return ruleNameRepository.findAll();
	}

	/**
	 * Find a Rule by its ID
	 *
	 * @param id								The Rule ID to find
	 * @return									Rule if it exists, otherwise an error is thrown
	 * @throws RuleNameServiceException	 		Thrown if the Rule was not found
	 */
	public RuleName findRuleNameById(Integer id) throws RuleNameServiceException {
		Optional<RuleName> ruleName = ruleNameRepository.findRuleNameById(id);
		if (id != null && ruleName.isPresent()) {
			return ruleName.get();
		}
		throw new RuleNameServiceException("Rule was not found with given ID");
	}

	/**
	 * Create a Rule
	 *
	 * @param ruleNameEntity					The Rule Entity to create
	 * @return									True if the creation was successful
	 * @throws RuleNameServiceException			Thrown if there was an error while creating the Rule
	 *
	 */
	public boolean createRuleName(RuleName ruleNameEntity) throws RuleNameServiceException {
		if (ruleNameEntity != null && !ruleNameRepository.findRuleNameById(ruleNameEntity.getId()).isPresent()) {
			ruleNameRepository.save(ruleNameEntity);
			log.info("[RULE SERVICE] Created a new rule with id '{}' and name '{}'", ruleNameEntity.getId(),
					ruleNameEntity.getName());
			return true;
		}
		throw new RuleNameServiceException("There was an error while creating the Rule");
	}

	/**
	 * Update an existing Rule
	 *
	 * @param id								The Rule ID to update
	 * @param ruleNameEntityUpdated				The new fields given for update
	 * @return									True if the update was successful
	 * @throws RuleNameServiceException			Thrown if Rule with given ID is not found
	 */
	public boolean updateRuleName(Integer id, RuleName ruleNameEntityUpdated) throws RuleNameServiceException {
		Optional<RuleName> ruleName = ruleNameRepository.findRuleNameById(id);
		if (id != null && ruleName.isPresent()) {
			ruleNameEntityUpdated.setId(id);
			ruleNameRepository.save(ruleNameEntityUpdated);

			log.info("[RULE SERVICE] Updated rule id '{}' with name '{}'", ruleNameEntityUpdated.getId(),
					ruleNameEntityUpdated.getName());
			return true;
		}
		throw new RuleNameServiceException("Could not find rule with id : " + id);
	}

	/**
	 * Delete a Rule
	 *
	 * @param id								The Rule ID to delete
	 * @return									True if the deletion was successful
	 * @throws RuleNameServiceException			Thrown if Rule with given ID is not found
	 */
	public boolean deleteRuleName(Integer id) throws RuleNameServiceException {
		Optional<RuleName> ruleName = ruleNameRepository.findRuleNameById(id);
		if (id != null && ruleName.isPresent()) {
			ruleNameRepository.delete(ruleName.get());
			log.info("[RULE SERVICE] Deleted rule id '{}'", id);
			return true;
		}
		throw new RuleNameServiceException("Could not find rule with id : " + id);
	}

}
