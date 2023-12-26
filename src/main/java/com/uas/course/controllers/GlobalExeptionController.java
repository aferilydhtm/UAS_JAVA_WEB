package com.uas.course.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExeptionController {

	@ExceptionHandler(value = Exception.class)
	public String exceptionHandler(Model model, Exception exception) {
		model.addAttribute("exceptionMsg", exception.getMessage());
		return "error";
	}
}
