package com.uas.course.controllers.adminControllers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import com.uas.course.models.Person;
import com.uas.course.models.StudyClass;
import com.uas.course.services.servicesImp.ClassServiceImp;
import com.uas.course.services.servicesImp.PersonServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminClassController {

	private final ClassServiceImp classService;
	private final PersonServiceImp personService;

	public AdminClassController(ClassServiceImp classService, PersonServiceImp personService) {
		this.classService = classService;
		this.personService = personService;
	}

	@GetMapping(value = { "/viewAllClasses/{curPage}" })
	public String viewAllClasses(Model model, @PathVariable int curPage,
			@RequestParam(required = false) List<String> errors, @RequestParam String sortBy,
			@RequestParam String sortDir) {
		Page<StudyClass> classes = classService.getAllClasses(curPage, sortBy, sortDir);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("newClass", new StudyClass());
		model.addAttribute("curPage", curPage);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("classes", classes.getContent());
		model.addAttribute("pagesCount", classes.getTotalPages());
		if (errors != null)
			model.addAttribute("errors", errors);

		return "admin/view-all-classes";
	}

	@PostMapping("/addNewClass")
	public String addNewClass(@Valid @ModelAttribute(name = "newClass") StudyClass studyClass, BindingResult errors,
			Model model) {
		if (errors.hasErrors()) {
			List<String> errorList = errors.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			// Encode the error messages before adding them to the URL
			String encodedErrors = UriUtils.encodeQueryParam(String.join(",", errorList), StandardCharsets.UTF_8);

			return "redirect:/admin/viewAllClasses/1?sortBy=name&sortDir=asc&errors=" + encodedErrors;
		} else {
			classService.addNewClass(studyClass);
			return "redirect:/admin/viewAllClasses/1?sortBy=name&sortDir=asc";
		}
	}

	@GetMapping("/viewClassMembers/{curPage}")
	public String viewClassMembers(@PathVariable int curPage, @RequestParam int classId,
			@RequestParam(required = false) String error, @RequestParam String sortBy, @RequestParam String sortDir,
			Model model) {

		Page<Person> members = classService.getClassMembers(classId, curPage, sortBy, sortDir);
		StudyClass studyClass = classService.getClassById(classId);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("members", members.getContent());
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("classId", classId);
		model.addAttribute("className", studyClass.getName());
		model.addAttribute("curPage", curPage);
		model.addAttribute("pagesCount", members.getTotalPages());

		if (error != null)
			model.addAttribute("error", "No member associated with this email!");

		return "admin/view-class-members"; // create it
	}

	@PostMapping("/addMemberToClass")
	public String addMemberToClass(@RequestParam int classId, HttpServletRequest request, Model model) {
		String memberEmail = request.getParameter("email");

		boolean success = personService.addMemberToClass(memberEmail, classId);

		if (!success) {
			return "redirect:/admin/viewClassMembers/1?sortBy=name&sortDir=asc&classId=" + classId + "&error=true";
		}
		return "redirect:/admin/viewClassMembers/1?sortBy=name&sortDir=asc&classId=" + classId;
	}
}
