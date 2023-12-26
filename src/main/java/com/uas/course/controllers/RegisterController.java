package com.uas.course.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import com.uas.course.models.Person;
import com.uas.course.services.servicesImp.ClassServiceImp;
import com.uas.course.services.servicesImp.PersonServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class RegisterController {

	private final ClassServiceImp classService;
	private final PersonServiceImp personService;

	public RegisterController(ClassServiceImp classService, PersonServiceImp personService) {
		this.classService = classService;
		this.personService = personService;
	}

	@GetMapping("/register")
	public String registerUser(Model model, @RequestParam(required = false) String errors,
			@RequestParam(required = false) boolean isExist) {
		model.addAttribute("person", new Person());
		model.addAttribute("classes", classService.getAllClasses());

		if (errors != null) {
			String errorString = UriUtils.decode(errors, StandardCharsets.UTF_8);
			List<String> errorList = Arrays.asList(errorString.split(","));

			model.addAttribute("errors", errorList);
		}
		if (isExist)
			model.addAttribute("isExist", "Repeated Email!, please login or use another email.");

		return "register";
	}

	@PostMapping("/createUser")
	public String createUser(@Valid @ModelAttribute Person person, Errors errors, HttpServletRequest request,
			Model model) {

		if (errors.hasErrors()) {
			List<String> fieldErrorList = errors.getFieldErrors().stream()
					.map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());

			List<String> globalErrorList = errors.getGlobalErrors().stream()
					.map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());

			fieldErrorList.addAll(globalErrorList);

			String encodedErrors = UriUtils.encodeQueryParam(String.join(",", fieldErrorList), StandardCharsets.UTF_8);

			return "redirect:/register?errors=" + encodedErrors;
		}

		if (personService.isExistUser(person.getEmail())) {
			return "redirect:/register?isExist=true";
		}

		int classId = Integer.parseInt(request.getParameter("classNum"));

		personService.createUser(person, classId);

		return "redirect:/login";
	}

}
