package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.Rule;
import com.poseidon.app.domain.dto.RuleDto;
import com.poseidon.app.exceptions.RuleServiceException;
import com.poseidon.app.repositories.RuleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RuleService {

	@Autowired
	RuleRepository ruleNameRepository;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Get a list of every rules
	 * @return									List<RuleName> with existing rules
	 */
	public List<Rule> findAllRules() {
		return ruleNameRepository.findAll();
	}

	/**
	 * Find a Rule by its ID
	 *
	 * @param id								The Rule ID to find
	 * @return									Rule if it exists, otherwise an error is thrown
	 * @throws RuleServiceException	 			Thrown if the Rule was not found
	 */
	public Rule findRuleById(Integer id) throws RuleServiceException {
		Optional<Rule> ruleName = ruleNameRepository.findRuleById(id);
		if (id != null && ruleName.isPresent()) {
			return ruleName.get();
		}
		throw new RuleServiceException("Could not find rule with id : " + id);
	}

	/**
	 * Create a Rule
	 *
	 * @param ruleEntity						The Rule Entity to create
	 * @return									True if the creation was successful
	 * @throws RuleServiceException				Thrown if there was an error while creating the Rule
	 *
	 */
	public boolean createRule(Rule ruleEntity) throws RuleServiceException {
		if (ruleEntity != null && !ruleNameRepository.findRuleById(ruleEntity.getId()).isPresent()) {
			ruleNameRepository.save(ruleEntity);
			log.info("[RULE SERVICE] Created a new rule with id '{}' and name '{}'", ruleEntity.getId(),
					ruleEntity.getName());
			return true;
		}
		throw new RuleServiceException("There was an error while creating the rule");
	}

	/**
	 * Update an existing Rule
	 *
	 * @param id								The Rule ID to update
	 * @param ruleEntityUpdated					The new fields given for update
	 * @return									True if the update was successful
	 * @throws RuleServiceException				Thrown if Rule with given ID is not found
	 */
	public boolean updateRule(Integer id, Rule ruleEntityUpdated) throws RuleServiceException {
		Optional<Rule> ruleName = ruleNameRepository.findRuleById(id);
		if (id != null && ruleName.isPresent()) {
			ruleEntityUpdated.setId(id);
			ruleNameRepository.save(ruleEntityUpdated);

			log.info("[RULE SERVICE] Updated rule id '{}' with name '{}'", ruleEntityUpdated.getId(),
					ruleEntityUpdated.getName());
			return true;
		}
		throw new RuleServiceException("Could not find rule with id : " + id);
	}

	/**
	 * Delete a Rule
	 *
	 * @param id								The Rule ID to delete
	 * @return									True if the deletion was successful
	 * @throws RuleServiceException				Thrown if Rule with given ID is not found
	 */
	public boolean deleteRule(Integer id) throws RuleServiceException {
		Optional<Rule> ruleName = ruleNameRepository.findRuleById(id);
		if (id != null && ruleName.isPresent()) {
			ruleNameRepository.delete(ruleName.get());
			log.info("[RULE SERVICE] Deleted rule id '{}'", id);
			return true;
		}
		throw new RuleServiceException("Could not find rule with id : " + id);
	}

	public Rule convertDtoToEntity(RuleDto ruleDto) {
		return modelMapper.map(ruleDto, Rule.class);
	}

	public RuleDto convertEntityToDto(Rule ruleEntity) {
		return modelMapper.map(ruleEntity, RuleDto.class);
	}

}
