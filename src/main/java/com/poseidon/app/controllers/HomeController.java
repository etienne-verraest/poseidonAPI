package com.poseidon.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	/**
	 * We ensure that the main page of the application is the bid list page.
	 * The Default Success Url on Spring security is also set on this url.
	 */
	@RequestMapping("/")
	public String home(Model model) {
		return "redirect:/bidList/list";
	}

}
