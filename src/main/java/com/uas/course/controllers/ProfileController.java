package com.uas.course.controllers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.uas.course.services.servicesImp.PersonServiceImp;
import com.uas.course.util.Profile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ProfileController {

	private final PersonServiceImp personService;

	public ProfileController(PersonServiceImp personService) {
		this.personService = personService;
	}

	@GetMapping("/viewProfile")
	public String viewProfile(@RequestParam(required = true) int userId,
			@RequestParam(required = false) String successUpdate, @RequestParam(required = false) List<String> errors,
			HttpSession session, Model model) {
		if (null == session.getAttribute("userId"))
			session.setAttribute("userId", userId);

		Profile profile = personService.initializeUserProfile(userId);

		model.addAttribute("userId", userId);
		model.addAttribute("profile", profile);
		model.addAttribute("photo", personService.getPhoto(userId));

		// ---- comes from the /updateProfile
		if (successUpdate != null) {
			model.addAttribute("successUpdate", "Profile Updated Successfully!");
		}
		if (errors != null) {
			model.addAttribute("errors", errors);
		}
		// --------
		return "profile";
	}

	@PostMapping("/updateProfile")
	public String updateProfile(@Valid @ModelAttribute Profile profile, Errors errors,
			@RequestParam(name = "personal_photo", required = false) MultipartFile photo, HttpSession session,
			Model model) throws HttpSessionRequiredException {

		if (null == session.getAttribute("userId"))
			throw new HttpSessionRequiredException("Session is ended or not available");

		int userId = (int) session.getAttribute("userId");

		if (errors.hasErrors()) {
			List<String> errorList = errors.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			// Encode the error messages before adding them to the URL
			String encodedErrors = UriUtils.encodeQueryParam(String.join(",", errorList), StandardCharsets.UTF_8);

			return "redirect:/viewProfile?userId=" + userId + "&errors=" + encodedErrors;
		}

		personService.updateUserProfile(profile, photo, userId);

		return "redirect:/viewProfile?userId=" + userId + "&successUpdate=true";
	}

}
