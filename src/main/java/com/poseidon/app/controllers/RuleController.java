package com.poseidon.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.app.config.BootstrapAlerts;
import com.poseidon.app.domain.Rule;
import com.poseidon.app.domain.dto.RuleDto;
import com.poseidon.app.exceptions.RuleServiceException;
import com.poseidon.app.services.RuleService;

@Controller
public class RuleController {

	@Autowired
	RuleService ruleService;

	/**
	 * Show the rules page
	 */
	@RequestMapping("/ruleName/list")
	public String home(Model model) {
		model.addAttribute("rules", ruleService.findAllRules());
		return "ruleName/list";
	}

	/**
	 * Show the form to add a Rule
	 *
	 * @param ruleDto							RuleDto with fields that are required to create a new rule
	 * @return									Add a Rule view
	 */
	@GetMapping("/ruleName/add")
	public String addRuleForm(RuleDto ruleDto) {
		return "ruleName/add";
	}

	/**
	 * Validate the fields of the "Add Rule" Form
	 *
	 * @param ruleDto							RuleDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of rules if the form was validated, otherwise show errors
	 * @throws RuleServiceException				Thrown if there is an error while creating the rule
	 */
	@PostMapping("/ruleName/validate")
	public String validate(@Valid RuleDto ruleDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws RuleServiceException {
		if (!result.hasErrors()) {

			Rule newRule = ruleService.convertDtoToEntity(ruleDto);
			ruleService.createRule(newRule);

			redirectAttributes.addFlashAttribute("message",
					String.format("Rule with id '%d' was successfully deleted", newRule.getId()));
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

			model.addAttribute("rules", ruleService.findAllRules());

			return "redirect:/ruleName/list";
		}
		return "ruleName/add";
	}

	/**
	 * Update a Rule
	 *
	 * @param id								The ID that is going to be updated
	 * @return								    The form to update a rule
	 * @throws RuleServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/ruleName/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws RuleServiceException {

		try {
			Rule ruleNameToUpdate = ruleService.findRuleById(id);
			RuleDto ruleDto = ruleService.convertEntityToDto(ruleNameToUpdate);
			ruleDto.setId(id);
			model.addAttribute("ruleDto", ruleDto);
		} catch (RuleServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
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
	 * @throws RuleServiceException			Thrown if there is an error while updating the rule
	 */
	@PostMapping("/ruleName/update/{id}")
	public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleDto ruleDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) throws RuleServiceException {

		if (result.hasErrors()) {
			return "ruleName/update";
		}

		Rule updatedRuleName = ruleService.convertDtoToEntity(ruleDto);
		ruleService.updateRule(id, updatedRuleName);

		redirectAttributes.addFlashAttribute("message",
				String.format("Rule with id '%d' was successfully updated", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

		model.addAttribute("rules", ruleService.findAllRules());

		return "redirect:/ruleName/list";
	}

	/**
	 * Delete a rule
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The rules list if the deletion was successful
	 * @throws RuleServiceException			Thrown if there was an error while deleting the given rule
	 */
	@GetMapping("/ruleName/delete/{id}")
	public String deleteRuleName(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws RuleServiceException {
		try {
			ruleService.deleteRule(id);
		} catch (RuleServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
			return "redirect:/ruleName/list";
		}

		redirectAttributes.addFlashAttribute("message",
				String.format("Rule with id '%d' was successfully deleted", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

		model.addAttribute("rules", ruleService.findAllRules());

		return "redirect:/ruleName/list";
	}
}
