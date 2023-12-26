package com.uas.course.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

	@GetMapping(value = { "", "/" })
	public String homeController() {
		return "login";
	}

	@GetMapping(value = { "/local" })
	public String changeLocal(@RequestParam(name = "lang") String lang, HttpServletRequest request) {
		String curPageUrl = request.getHeader("Referer");
		return "redirect:" + curPageUrl.substring(21);
	}
}
