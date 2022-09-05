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

	@RequestMapping("/user/list")
	public String home(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "user/list";
	}

	@GetMapping("/user/add")
	public String addUser(UserDto userDto) {
		return "user/add";
	}

	@PostMapping("/user/validate")
	public String validate(@Valid UserDto userDto, BindingResult result, Model model) throws UserServiceException {

		if (!result.hasErrors()) {
			// If the validation went well, we map the Dto to a new user Entity
			// We then call the user service to create the user
			User newUser = modelMapper.map(userDto, User.class);
			userService.createUser(newUser);

			model.addAttribute("users", userService.findAllUsers());
			return "redirect:/user/list";
		}
		return "user/add";
	}

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

	@PostMapping("/user/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, @Valid UserDto user, BindingResult result, Model model)
			throws UserServiceException {

		if (result.hasErrors()) {
			return "user/update";
		}

		User newUser = modelMapper.map(user, User.class);
		userService.updateUser(id, newUser);

		model.addAttribute("users", userService.findAllUsers());
		return "redirect:/user/list";
	}

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
