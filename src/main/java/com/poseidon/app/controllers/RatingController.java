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

import com.poseidon.app.config.constants.BootstrapAlerts;
import com.poseidon.app.domain.Rating;
import com.poseidon.app.domain.dto.RatingDto;
import com.poseidon.app.exceptions.RatingServiceException;
import com.poseidon.app.services.RatingService;

@Controller
public class RatingController {

	@Autowired
	RatingService ratingService;

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
	public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws RatingServiceException {
		if (!result.hasErrors()) {
			Rating newRating = ratingService.convertDtoToEntity(ratingDto);
			ratingService.createRating(newRating);

			redirectAttributes.addFlashAttribute("message",
					String.format("Rating with id '%d' was successfully created", newRating.getId()));
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

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
	public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws RatingServiceException {
		try {
			Rating ratingToUpdate = ratingService.findRatingById(id);
			RatingDto ratingDto = ratingService.convertEntityToDto(ratingToUpdate);
			ratingDto.setId(id);
			model.addAttribute("ratingDto", ratingDto);
		} catch (RatingServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
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
			Model model, RedirectAttributes redirectAttributes) throws RatingServiceException {

		if (result.hasErrors()) {
			return "rating/update";
		}
		Rating updatedRating = ratingService.convertDtoToEntity(ratingDto);
		ratingService.updateRating(id, updatedRating);

		redirectAttributes.addFlashAttribute("message",
				String.format("Rating with id '%d' was successfully updated", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

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
	public String deleteRating(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws RatingServiceException {
		try {
			ratingService.deleteRating(id);
		} catch (RatingServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
			return "redirect:/rating/list";
		}

		redirectAttributes.addFlashAttribute("message",
				String.format("Rating with id '%d' was successfully deleted", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);

		model.addAttribute("ratings", ratingService.findAllRatings());
		return "redirect:/rating/list";
	}
}
