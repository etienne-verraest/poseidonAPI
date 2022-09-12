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
import com.poseidon.app.domain.Trade;
import com.poseidon.app.domain.dto.TradeDto;
import com.poseidon.app.exceptions.TradeServiceException;
import com.poseidon.app.services.TradeService;

@Controller
public class TradeController {

	@Autowired
	TradeService tradeService;

	/**
	 * Show the trades page
	 */
	@RequestMapping("/trade/list")
	public String home(Model model) {

		model.addAttribute("trades", tradeService.findAllTrades());

		return "trade/list";
	}

	/**
	 * Show the form to add a Trade
	 *
	 * @param tradeDto							TradeDto with fields that are required to create a new rule
	 * @return									Add a Trade view
	 */
	@GetMapping("/trade/add")
	public String addUser(TradeDto tradeDto) {

		return "trade/add";
	}

	/**
	 * Validate the fields of the "Add Trade" Form
	 *
	 * @param tradeDto							TradeDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of trades if the form was validated, otherwise show errors
	 * @throws TradeServiceException			Thrown if there is an error while creating the trade
	 */
	@PostMapping("/trade/validate")
	public String validate(@Valid TradeDto tradeDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws TradeServiceException {

		if (!result.hasErrors()) {
			Trade newTrade = tradeService.convertDtoToEntity(tradeDto);
			tradeService.createTrade(newTrade);

			redirectAttributes.addFlashAttribute("message",
					String.format("Trade with id '%d' was successfully created", newTrade.getId()));
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

			model.addAttribute("trades", tradeService.findAllTrades());

			return "redirect:/trade/list";
		}

		return "trade/add";
	}

	/**
	 * Update a Trade
	 *
	 * @param id								The ID that is going to be updated
	 * @return								    The form to update a trade
	 * @throws TradeServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/trade/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws TradeServiceException {

		try {
			Trade tradeToUpdate = tradeService.findTradeById(id);
			TradeDto tradeDto = tradeService.convertEntityToDto(tradeToUpdate);
			tradeDto.setId(id);
			model.addAttribute("tradeDto", tradeDto);
		} catch (TradeServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
			return "redirect:/trade/list";
		}

		return "trade/update";
	}

	/**
	 * Validate the fields for trade updating
	 *
	 * @param id								The ID that is going to be updated
	 * @param tradeDto							The new fields that will be mapped to the existing trade
	 * @return									Returns the list of trades if the form is valid, otherwise show errors
	 * @throws TradeServiceException			Thrown if there is an error while updating the trade
	 */
	@PostMapping("/trade/update/{id}")
	public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto tradeDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) throws TradeServiceException {

		if (result.hasErrors()) {
			return "trade/update";
		}

		Trade updatedTrade = tradeService.convertDtoToEntity(tradeDto);
		tradeService.updateTrade(id, updatedTrade);

		redirectAttributes.addFlashAttribute("message",
				String.format("Trade with id '%d' was successfully updated", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

		model.addAttribute("trades", tradeService.findAllTrades());

		return "redirect:/trade/list";
	}

	/**
	 * Delete a trade
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The trades list if the deletion was successful
	 * @throws TradeServiceException			Thrown if there was an error while deleting the given trade
	 */
	@GetMapping("/trade/delete/{id}")
	public String deleteTrade(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws TradeServiceException {

		try {
			tradeService.deleteTrade(id);
		} catch (TradeServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);

			return "redirect:/trade/list";
		}

		redirectAttributes.addFlashAttribute("message",
				String.format("Trade with id '%d' was successfully deleted", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

		model.addAttribute("trades", tradeService.findAllTrades());

		return "redirect:/trade/list";
	}
}
