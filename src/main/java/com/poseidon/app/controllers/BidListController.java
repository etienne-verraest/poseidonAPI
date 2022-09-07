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

import com.poseidon.app.domain.Bid;
import com.poseidon.app.domain.dto.BidDto;
import com.poseidon.app.exceptions.BidServiceException;
import com.poseidon.app.services.BidService;

@Controller
public class BidListController {

	@Autowired
	BidService bidService;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Show the BidList page
	 */
	@RequestMapping("/bidList/list")
	public String home(Model model) {
		model.addAttribute("bids", bidService.findAllBids());
		return "bidList/list";
	}

	/**
	 * Show the form to add a bid
	 *
	 * @param bidListDto						BidListDto with fields that are required to create a new bid
	 * @return									Add a bid view
	 */
	@GetMapping("/bidList/add")
	public String addBidForm(BidDto bidListDto) {
		return "bidList/add";
	}

	/**
	 * Validate the fields of the "Add Bid" Form
	 *
	 * @param bidDto							BidDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of bids if the form was validated, otherwise show errors
	 * @throws BidServiceException			Thrown if there is an error while creating the bid
	 */
	@PostMapping("/bidList/validate")
	public String validate(@Valid BidDto bidDto, BindingResult result, Model model) throws BidServiceException {
		if (!result.hasErrors()) {
			Bid newBidList = modelMapper.map(bidDto, Bid.class);
			bidService.createBid(newBidList);
			model.addAttribute("bids", bidService.findAllBids());
			return "redirect:/bidList/list";
		}
		return "bidList/add";
	}

	/**
	 * Update a bid
	 *
	 * @param id								The bid ID that is going to be updated
	 * @return								    The form to update a bid
	 * @throws BidServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/bidList/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

		try {
			Bid bidToUpdate = bidService.findBidById(id);
			BidDto bidDto = modelMapper.map(bidToUpdate, BidDto.class);
			model.addAttribute("bidDto", bidDto);
		} catch (BidServiceException error) {
			return "redirect:/bidList/list";
		}

		return "bidList/update";
	}

	/**
	 * Validate the fields for bid updating
	 *
	 * @param id								The ID that is going to be updated
	 * @param bidListDto						The new fields that will be mapped to the existing bid
	 * @return									Returns the list of bids if the form was validated, otherwise show errors
	 * @throws BidServiceException			Thrown if there is an error while updating the bid
	 */
	@PostMapping("/bidList/update/{id}")
	public String updateBid(@PathVariable("id") Integer id, @Valid BidDto bidDto, BindingResult result, Model model)
			throws BidServiceException {

		if (result.hasErrors()) {
			return "bidList/update";
		}

		Bid newBid = modelMapper.map(bidDto, Bid.class);
		bidService.updateBid(id, newBid);
		model.addAttribute("bids", bidService.findAllBids());
		return "redirect:/bidList/list";
	}

	/**
	 * Delete a bid
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The bid list if the deletion was successful
	 * @throws BidServiceException			Thrown if there was an error while deleting the bid
	 */
	@GetMapping("/bidList/delete/{id}")
	public String deleteBid(@PathVariable("id") Integer id, Model model) {

		try {
			bidService.deleteBid(id);
		} catch (BidServiceException error) {
			return "redirect:/bidList/list";
		}
		model.addAttribute("bids", bidService.findAllBids());
		return "redirect:/bidList/list";
	}
}
