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

import com.poseidon.app.domain.User;
import com.poseidon.app.domain.dto.UserDto;
import com.poseidon.app.exceptions.UserServiceException;
import com.poseidon.app.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Show the users registered on the website
	 */
	@RequestMapping("/user/list")
	public String home(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "user/list";
	}

	/**
	 * Show the form to add an user
	 *
	 * @param userDto							UserDto with fields that are required to create a new user
	 * @return									Add an user view
	 */
	@GetMapping("/user/add")
	public String addUser(UserDto userDto) {
		return "user/add";
	}

	/**
	 * Validate the fields of the "Add User" Form
	 *
	 * @param userDto							UserDto with filled fields that are going to be validated
	 * @param result							The result of the validation
	 * @return									Returns the list of users if the form was validated, otherwise show errors
	 * @throws UserServiceException				Thrown if there is an error while creating the user
	 */
	@PostMapping("/user/validate")
	public String validate(@Valid UserDto userDto, BindingResult result, Model model) throws UserServiceException {

		if (!result.hasErrors()) {
			User newUser = modelMapper.map(userDto, User.class);
			userService.createUser(newUser);
			model.addAttribute("users", userService.findAllUsers());
			return "redirect:/user/list";
		}
		return "user/add";
	}

	/**
	 * Show the form to update an existing user
	 *
	 * @param id								The user ID that is going to be updated
	 * @return								    The form to update an user
	 * @throws UserServiceException				Thrown if the given user ID was not found in database
	 */
	@GetMapping("/user/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws UserServiceException {
		try {
			User userToUpdate = userService.findUserById(id);
			UserDto user = modelMapper.map(userToUpdate, UserDto.class);
			user.setPassword("");
			model.addAttribute("userDto", user);
		} catch (UserServiceException error) {
			return "redirect:/user/list?update_error";
		}
		return "user/update";
	}

	/**
	 * Validate the fields for the update of user
	 *
	 * @param id								The user ID that is going to be updated
	 * @param userDto							The new fields that will be mapped to the existing user
	 * @return									Returns the list of users if the form was validated, otherwise show errors
	 * @throws UserServiceException				Thrown if there is an error while updating the user
	 */
	@PostMapping("/user/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, @Valid UserDto userDto, BindingResult result, Model model)
			throws UserServiceException {

		if (result.hasErrors()) {
			return "user/update";
		}
		User newUser = modelMapper.map(userDto, User.class);
		userService.updateUser(id, newUser);
		model.addAttribute("users", userService.findAllUsers());
		return "redirect:/user/list";
	}

	/**
	 * Delete a user by its ID
	 *
	 * @param id								The user ID that is going to be deleted
	 * @return									The users list if the deletion was successful
	 * @throws UserServiceException				Thrown if there was an error while deleting the users
	 */
	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model) throws UserServiceException {

		try {
			userService.deleteUser(id);
		} catch (UserServiceException error) {
			log.error("[User Service] {}", error.getMessage());
			return "redirect:/user/list";
		}
		model.addAttribute("users", userService.findAllUsers());
		return "redirect:/user/list";
	}
}
