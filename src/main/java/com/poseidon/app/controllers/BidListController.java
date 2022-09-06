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

import com.poseidon.app.domain.BidList;
import com.poseidon.app.domain.dto.BidListDto;
import com.poseidon.app.exceptions.BidListServiceException;
import com.poseidon.app.services.BidListService;

@Controller
public class BidListController {

	@Autowired
	BidListService bidListService;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Show the BidList page
	 */
	@RequestMapping("/bidList/list")
	public String home(Model model) {
		model.addAttribute("bids", bidListService.findAllBidList());
		return "bidList/list";
	}

	/**
	 * Show the form to add a bid
	 *
	 * @param bidListDto						BidListDto with fields that are required to create a new bid
	 * @return									Add a bid view
	 */
	@GetMapping("/bidList/add")
	public String addBidForm(BidListDto bidListDto) {
		return "bidList/add";
	}

	/**
	 * Validate the fields of the "Add Bid" Form
	 *
	 * @param bidListDto						BidListDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of bids if the form was validated, otherwise show errors
	 * @throws BidListServiceException			Thrown if there is an error while creating the bid
	 */
	@PostMapping("/bidList/validate")
	public String validate(@Valid BidListDto bidListDto, BindingResult result, Model model) throws BidListServiceException {
		if (!result.hasErrors()) {
			BidList newBidList = modelMapper.map(bidListDto, BidList.class);
			bidListService.createBidList(newBidList);
			model.addAttribute("bids", bidListService.findAllBidList());
			return "redirect:/bidList/list";
		}
		return "bidList/add";
	}

	/**
	 * Update a bid
	 *
	 * @param id								The bid ID that is going to be updated
	 * @return								    The form to update a bid
	 * @throws BidListServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/bidList/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

		try {
			BidList bidListToUpdate = bidListService.findBidListById(id);
			BidListDto bidList = modelMapper.map(bidListToUpdate, BidListDto.class);
			model.addAttribute("bidListDto", bidList);
		} catch (BidListServiceException error) {
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
	 * @throws BidListServiceException			Thrown if there is an error while updating the bid
	 */
	@PostMapping("/bidList/update/{id}")
	public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidListDto, BindingResult result,
			Model model) throws BidListServiceException {

		if (result.hasErrors()) {
			return "bidList/update";
		}

		BidList newBid = modelMapper.map(bidListDto, BidList.class);
		bidListService.updateBidList(id, newBid);
		model.addAttribute("bids", bidListService.findAllBidList());
		return "redirect:/bidList/list";
	}

	/**
	 * Delete a bid
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The bid list if the deletion was successful
	 * @throws BidListServiceException			Thrown if there was an error while deleting the bid
	 */
	@GetMapping("/bidList/delete/{id}")
	public String deleteBid(@PathVariable("id") Integer id, Model model) {

		try {
			bidListService.deleteBidList(id);
		} catch (BidListServiceException error) {
			return "redirect:/bidList/list";
		}
		model.addAttribute("bids", bidListService.findAllBidList());
		return "redirect:/bidList/list";
	}
}
