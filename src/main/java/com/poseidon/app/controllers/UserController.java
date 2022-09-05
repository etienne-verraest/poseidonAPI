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

import com.poseidon.app.domain.User;
import com.poseidon.app.exceptions.UserServiceException;
import com.poseidon.app.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/user/list")
	public String home(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "user/list";
	}

	@GetMapping("/user/add")
	public String addUser(User bid) {
		return "user/add";
	}

	@PostMapping("/user/validate")
	public String validate(@Valid User user, BindingResult result, Model model) throws UserServiceException {
		if (!result.hasErrors()) {
			userService.createUser(user);
			model.addAttribute("users", userService.findAllUsers());
			return "redirect:/user/list";
		}
		return "user/add";
	}

	@GetMapping("/user/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws UserServiceException {
		User user = userService.findUserById(id);
		user.setPassword("");
		model.addAttribute("user", user);
		return "user/update";
	}

	@PostMapping("/user/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model)
			throws UserServiceException {

		if (result.hasErrors()) {
			return "user/update";
		}

		userService.updateUser(id, user);
		model.addAttribute("users", userService.findAllUsers());
		return "redirect:/user/list";
	}

	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model) throws UserServiceException {
		userService.deleteUser(id);
		model.addAttribute("users", userService.findAllUsers());
		return "redirect:/user/list";
	}
}
