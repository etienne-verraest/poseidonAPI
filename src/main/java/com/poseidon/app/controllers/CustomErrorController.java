package com.poseidon.app.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@GetMapping("/error")
	public String handleError(HttpServletResponse response, Model model) {

		String errorMessage = String.format("Oops ! You have found a %d error", response.getStatus());
		model.addAttribute("errorMsg", errorMessage);
		return "error";
	}
}
