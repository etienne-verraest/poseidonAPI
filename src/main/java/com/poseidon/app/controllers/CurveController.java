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
import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.domain.dto.CurvePointDto;
import com.poseidon.app.exceptions.CurvePointServiceException;
import com.poseidon.app.services.CurvePointService;

@Controller
public class CurveController {

	@Autowired
	CurvePointService curvePointService;

	/**
	 * Show the Curve Points page
	 */
	@RequestMapping("/curvePoint/list")
	public String home(Model model) {
		model.addAttribute("curvePoints", curvePointService.findAllCurvePoint());
		return "curvePoint/list";
	}

	/**
	 * Show the form to add a Curve Point
	 *
	 * @param curvePointDto						CurvePointDto with fields that are required to create a new Curve Point
	 * @return									Add a Curve Point view
	 */
	@GetMapping("/curvePoint/add")
	public String addCurvePointForm(CurvePointDto curvePointDto) {
		return "curvePoint/add";
	}

	/**
	 * Validate the fields of the "Add Curve Point" Form
	 *
	 * @param curvePointDto						curvePointDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of curve points if the form was validated, otherwise show errors
	 * @throws CurvePointServiceException		Thrown if there is an error while creating the curve point
	 */
	@PostMapping("/curvePoint/validate")
	public String validate(@Valid CurvePointDto curvePointDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws CurvePointServiceException {

		if (!result.hasErrors()) {
			CurvePoint newCurvePoint = curvePointService.convertDtoToEntity(curvePointDto);
			curvePointService.createCurvePoint(newCurvePoint);
			model.addAttribute("curvePoints", curvePointService.findAllCurvePoint());
			redirectAttributes.addFlashAttribute("message",
					String.format("Curve Point with id '%d' was successfully created", newCurvePoint.getId()));
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);
			return "redirect:/curvePoint/list";
		}
		return "curvePoint/add";
	}

	/**
	 * Update a Curve Point
	 *
	 * @param id								The ID that is going to be updated
	 * @return								    The form to update a curve point
	 * @throws CurvePointServiceException		Thrown if the given ID was not found in database
	 */
	@GetMapping("/curvePoint/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws CurvePointServiceException {

		try {
			CurvePoint curvePointToUpdate = curvePointService.findCurvePointById(id);
			CurvePointDto curvePointDto = curvePointService.convertEntityToDto(curvePointToUpdate);
			curvePointDto.setId(id);
			model.addAttribute("curvePointDto", curvePointDto);
		} catch (CurvePointServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
			return "redirect:/curvePoint/list";
		}

		return "curvePoint/update";
	}

	/**
	 * Validate the fields for curve point updating
	 *
	 * @param id								The ID that is going to be updated
	 * @param curvePointDto						The new fields that will be mapped to the existing curve point
	 * @return									Returns the list of curvepoint if the form is valid, otherwise show errors
	 * @throws CurvePointServiceException		Thrown if there is an error while updating the curve point
	 */
	@PostMapping("/curvePoint/update/{id}")
	public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePointDto curvePointDto,
			BindingResult result, Model model, RedirectAttributes redirectAttributes)
			throws CurvePointServiceException {

		if (result.hasErrors()) {
			return "curvePoint/update";
		}

		CurvePoint updatedCurvePoint = curvePointService.convertDtoToEntity(curvePointDto);
		curvePointService.updateCurvePoint(id, updatedCurvePoint);
		model.addAttribute("curvePoints", curvePointService.findAllCurvePoint());
		redirectAttributes.addFlashAttribute("message",
				String.format("Curve Point with id '%d' was successfully updated", id));
		redirectAttributes.addFlashAttribute("message_type", "alert-primary");
		return "redirect:/curvePoint/list";
	}

	/**
	 * Delete a curve point
	 *
	 * @param id								The ID that is going to be deleted
	 * @return									The curve points list if the deletion was successful
	 * @throws CurvePointServiceException		Thrown if there was an error while deleting the curve point
	 */
	@GetMapping("/curvePoint/delete/{id}")
	public String deleteCurvePoint(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws CurvePointServiceException {
		try {
			curvePointService.deleteCurvePoint(id);
		} catch (CurvePointServiceException error) {
			redirectAttributes.addFlashAttribute("message", error.getMessage());
			redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.WARNING);
			return "redirect:/curvePoint/list";
		}
		redirectAttributes.addFlashAttribute("message",
				String.format("Curve Point with id '%d' was successfully deleted", id));
		redirectAttributes.addFlashAttribute("message_type", BootstrapAlerts.PRIMARY);
		model.addAttribute("curvePoints", curvePointService.findAllCurvePoint());
		return "redirect:/curvePoint/list";
	}
}
