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

import com.poseidon.app.domain.Rating;
import com.poseidon.app.domain.dto.RatingDto;
import com.poseidon.app.exceptions.RatingServiceException;
import com.poseidon.app.services.RatingService;

@Controller
public class RatingController {

	@Autowired
	RatingService ratingService;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * Show the ratings page
	 */
	@RequestMapping("/rating/list")
	public String home(Model model) {
		model.addAttribute("ratings", ratingService.findAllRatings());
		return "rating/list";
	}

	/**
	 * Show the form to add a Rating
	 *
	 * @param ratingDto							RatingDto with fields that are required to create a new rating
	 * @return									Add a Rating view
	 */
	@GetMapping("/rating/add")
	public String addRatingForm(RatingDto ratingDto) {
		return "rating/add";
	}

	/**
	 * Validate the fields of the "Add Rating" Form
	 *
	 * @param ratingDto							RatingDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of ratings if the form was validated, otherwise show errors
	 * @throws RatingServiceException		Thrown if there is an error while creating the rating
	 */
	@PostMapping("/rating/validate")
	public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model)
			throws RatingServiceException {
		if (!result.hasErrors()) {
			Rating newRating = modelMapper.map(ratingDto, Rating.class);
			ratingService.createRating(newRating);
			model.addAttribute("ratings", ratingService.findAllRatings());
			return "redirect:/rating/list";
		}
		return "rating/add";
	}

	/**
	 * Update a Rating
	 *
	 * @param id								The ID that is going to be updated
	 * @return								    The form to update a rating
	 * @throws RatingServiceException			Thrown if the given ID was not found in database
	 */
	@GetMapping("/rating/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws RatingServiceException {
		try {
			Rating ratingToUpdate = ratingService.findRatingById(id);
			RatingDto ratingDto = modelMapper.map(ratingToUpdate, RatingDto.class);
			ratingDto.setId(id);
			model.addAttribute("ratingDto", ratingDto);
		} catch (RatingServiceException error) {
			return "redirect:/rating/list";
		}
		return "rating/update";
	}

	/**
	 * Validate the fields for rating updating
	 *
	 * @param id								The ID that is going to be updated
	 * @param ratingDto							The new fields that will be mapped to the existing rating
	 * @return									Returns the list of ratings if the form is valid, otherwise show errors
	 * @throws RatingServiceException			Thrown if there is an error while updating the rating
	 */
	@PostMapping("/rating/update/{id}")
	public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto ratingDto, BindingResult result,
			Model model) throws RatingServiceException {

		if (result.hasErrors()) {
			return "rating/update";
		}
		Rating updatedRating = modelMapper.map(ratingDto, Rating.class);
		ratingService.updateRating(id, updatedRating);
		model.addAttribute("ratings", ratingService.findAllRatings());
		return "redirect:/rating/list";
	}

	/**
	 * Delete a rating
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The rating list if the deletion was successful
	 * @throws RatingServiceException			Thrown if there was an error while deleting the rating
	 */
	@GetMapping("/rating/delete/{id}")
	public String deleteRating(@PathVariable("id") Integer id, Model model) throws RatingServiceException {
		try {
			ratingService.deleteRating(id);
		} catch (RatingServiceException error) {
			return "redirect:/rating/list";
		}
		model.addAttribute("ratings", ratingService.findAllRatings());
		return "redirect:/rating/list";
	}
}
