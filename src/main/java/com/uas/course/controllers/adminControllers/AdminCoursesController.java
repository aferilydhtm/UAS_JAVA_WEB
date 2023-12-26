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

import com.uas.course.models.Course;
import com.uas.course.models.Person;
import com.uas.course.services.servicesImp.CourseServiceImp;
import com.uas.course.services.servicesImp.PersonServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminCoursesController {

	private final CourseServiceImp courseService;
	private final PersonServiceImp personService;

	public AdminCoursesController(CourseServiceImp courseService, PersonServiceImp personService) {
		this.courseService = courseService;
		this.personService = personService;
	}

	@GetMapping("/viewAllCourses/{curPage}")
	public String viewAllCourses(@PathVariable int curPage, Model model, @RequestParam String sortBy,
			@RequestParam String sortDir, @RequestParam(required = false) List<String> errors) {

		Page<Course> courses = courseService.getAllCourses(curPage, sortBy, sortDir);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("courses", courses.getContent());
		model.addAttribute("newCourse", new Course());
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("pagesCount", courses.getTotalPages());
		model.addAttribute("curPage", curPage);

		if (errors != null)
			model.addAttribute("errors", errors);

		return "admin/view-all-courses";
	}

	@PostMapping("/addNewCourse")
	public String addNewCourse(@Valid @ModelAttribute(name = "newCourse") Course course, BindingResult errors,
			Model model) {
		if (errors.hasErrors()) {
			List<String> errorList = errors.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			// Encode the error messages before adding them to the URL
			String encodedErrors = UriUtils.encodeQueryParam(String.join(",", errorList), StandardCharsets.UTF_8);

			return "redirect:/admin/viewAllCourses/1?sortBy=name&sortDir=asc&errors=" + encodedErrors;
		} else {
			courseService.addNewCourse(course);
			return "redirect:/admin/viewAllCourses/1?sortBy=name&sortDir=asc";
		}
	}

	@GetMapping("/deleteCourse")
	public String deleteCourse(@RequestParam int courseId) {
		personService.withdrawAllFromCourse(courseId);
		courseService.deleteCourse(courseId);

		return "redirect:/admin/viewAllCourses/1?sortBy=name&sortDir=asc";
	}

	@GetMapping("/viewCourseMembers/{curPage}")
	public String viewCourseMembers(@PathVariable int curPage, @RequestParam int courseId, @RequestParam String sortBy,
			@RequestParam String sortDir, @RequestParam(required = false) String error, Model model) {

		Page<Person> members = courseService.getCourseMembers(curPage, courseId, sortBy, sortDir);
		Course course = courseService.getCourseById(courseId);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("members", members.getContent());
		model.addAttribute("pagesCount", members.getTotalPages());
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("courseId", courseId);
		model.addAttribute("courseName", course.getName());
		model.addAttribute("curPage", curPage);

		if (error != null)
			model.addAttribute("error", "No member associated with this email!");

		return "admin/view-course-members";
	}

	@PostMapping("/addMemberToCourse")
	public String addMemberToCourse(@RequestParam int courseId, HttpServletRequest request, Model model) {
		String memberEmail = request.getParameter("email");

		boolean success = personService.addMemberToCourse(memberEmail, courseId);

		if (!success) {
			return "redirect:/admin/viewCourseMembers/1?sortBy=name&sortDir=asc&courseId=" + courseId + "&error=true";
		}

		return "redirect:/admin/viewCourseMembers/1?sortBy=name&sortDir=asc&courseId=" + courseId;
	}

	@GetMapping("/deleteMemberFromCourse")
	public String deleteMemberFromCourse(@RequestParam int memberId, @RequestParam int courseId) {

		courseService.deleteMemberFromCourse(memberId, courseId);
		personService.deleteCourseFromMember(memberId, courseId);

		return "redirect:/admin/viewCourseMembers/1?sortBy=name&sortDir=asc&courseId=" + courseId;
	}

}
