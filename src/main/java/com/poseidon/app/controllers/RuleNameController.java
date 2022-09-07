package com.poseidon.app.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.domain.dto.RuleNameDto;
import com.poseidon.app.exceptions.RuleNameServiceException;
import com.poseidon.app.services.RuleNameService;

@Controller
public class RuleNameController {

	@Autowired
	RuleNameService ruleNameService;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Show the rules page
	 */
	@RequestMapping("/ruleName/list")
	public String home(Model model) {
		model.addAttribute("rules", ruleNameService.findAllRuleNames());
		return "ruleName/list";
	}

	/**
	 * Show the form to add a Rule
	 *
	 * @param ruleNameDto						RuleNameDto with fields that are required to create a new rule
	 * @return									Add a Rule view
	 */
	@GetMapping("/ruleName/add")
	public String addRuleForm(RuleNameDto ruleNameDto) {
		return "ruleName/add";
	}

	/**
	 * Validate the fields of the "Add Rule" Form
	 *
	 * @param ruleNameDto						RuleNameDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of rules if the form was validated, otherwise show errors
	 * @throws RuleNameServiceException			Thrown if there is an error while creating the rule
	 */
	@PostMapping("/ruleName/validate")
	public String validate(@Valid RuleNameDto ruleNameDto, BindingResult result, Model model)
			throws RuleNameServiceException {
		if (!result.hasErrors()) {
			RuleName newRuleName = modelMapper.map(ruleNameDto, RuleName.class);
			ruleNameService.createRuleName(newRuleName);
			model.addAttribute("rules", ruleNameService.findAllRuleNames());
			return "redirect:/ruleName/list";
		}
		return "ruleName/add";
	}

	/**
	 * Update a Rule
	 *
	 * @param id								The ID that is going to be updated
	 * @return								    The form to update a rule
	 * @throws RuleNameServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/ruleName/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws RuleNameServiceException {

		try {
			RuleName ruleNameToUpdate = ruleNameService.findRuleNameById(id);
			RuleNameDto ruleNameDto = modelMapper.map(ruleNameToUpdate, RuleNameDto.class);
			ruleNameDto.setId(id);
			model.addAttribute("ruleNameDto", ruleNameDto);
		} catch (RuleNameServiceException error) {
			return "redirect:/ruleName/list";
		}
		return "ruleName/update";
	}

	/**
	 * Validate the fields for rule updating
	 *
	 * @param id								The ID that is going to be updated
	 * @param ruleNameDto						The new fields that will be mapped to the existing rule
	 * @return									Returns the list of rules if the form is valid, otherwise show errors
	 * @throws RuleNameServiceException			Thrown if there is an error while updating the rule
	 */
	@PostMapping("/ruleName/update/{id}")
	public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameDto ruleNameDto, BindingResult result,
			Model model) throws RuleNameServiceException {

		if (result.hasErrors()) {
			return "ruleName/update";
		}

		RuleName updatedRuleName = modelMapper.map(ruleNameDto, RuleName.class);
		ruleNameService.updateRuleName(id, updatedRuleName);
		model.addAttribute("rules", ruleNameService.findAllRuleNames());
		return "redirect:/ruleName/list";
	}

	/**
	 * Delete a rule
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The rules list if the deletion was successful
	 * @throws RuleNameServiceException			Thrown if there was an error while deleting the given rule
	 */
	@GetMapping("/ruleName/delete/{id}")
	public String deleteRuleName(@PathVariable("id") Integer id, Model model) throws RuleNameServiceException {
		try {
			ruleNameService.deleteRuleName(id);
		} catch (RuleNameServiceException error) {
			return "redirect:/ruleName/list";
		}
		model.addAttribute("rules", ruleNameService.findAllRuleNames());
		return "redirect:/ruleName/list";
	}
}
