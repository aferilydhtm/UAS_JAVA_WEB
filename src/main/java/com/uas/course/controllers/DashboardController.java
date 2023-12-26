package com.uas.course.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uas.course.models.Person;
import com.uas.course.services.servicesImp.PersonServiceImp;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	private final PersonServiceImp personService;

	public DashboardController(PersonServiceImp personService) {
		this.personService = personService;
	}

	@GetMapping("/dashboard")
	public String getDashboardPage(Authentication authentication, HttpSession session, Model model) {
		Person person = personService.getPerson(authentication.getName());
		model.addAttribute("person", person);
		session.setAttribute("photo", person.getPhoto());
		session.setMaxInactiveInterval(15 * 60);
		return "dashboard";
	}
}
