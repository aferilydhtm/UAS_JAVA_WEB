package com.uas.course.controllers;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginLogoutController {

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, HttpServletRequest request, Model model) {
		String successLogout = (String) request.getAttribute("successLogout");
		if (successLogout != null)
			model.addAttribute("successLogout", successLogout);
		if (error != null)
			model.addAttribute("error", "Invalid Credintials!");

		return "login";
	}

	@GetMapping("/logout")
	public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		new SecurityContextLogoutHandler().logout(request, response, authentication);

		request.setAttribute("successLogout", "You have been logged out successfully!");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);
	}
}
