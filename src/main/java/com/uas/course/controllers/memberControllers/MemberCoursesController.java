package com.uas.course.controllers.memberControllers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uas.course.models.Course;
import com.uas.course.models.Person;
import com.uas.course.services.servicesImp.CourseServiceImp;
import com.uas.course.services.servicesImp.PersonServiceImp;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberCoursesController {

	private final PersonServiceImp personService;
	private final CourseServiceImp courseService;

	public MemberCoursesController(PersonServiceImp personService, CourseServiceImp courseService) {
		this.personService = personService;
		this.courseService = courseService;
	}

	@GetMapping("/viewEnrolledCourses/{curPage}")
	public String viewEnrolledCourses(@PathVariable int curPage, @RequestParam int userId, @RequestParam String sortBy,
			@RequestParam String sortDir, HttpSession session, Model model) {

		if (null == session.getAttribute("userId"))
			session.setAttribute("userId", userId);

		Page<Course> courses = courseService.getEnrolledCourses(userId, curPage, sortBy, sortDir);
		Person member = personService.getPerson(userId);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("courses", courses.getContent());
		model.addAttribute("pagesCount", courses.getTotalPages());
		model.addAttribute("curPage", curPage);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("userId", userId);
		model.addAttribute("memberName", member.getName());

		return "member/enrolled_courses.html";
	}

	@GetMapping("/withdraw")
	public String withdraw(@RequestParam int courseId, HttpSession session, Model model)
			throws HttpSessionRequiredException {

		if (null == session.getAttribute("userId"))
			throw new HttpSessionRequiredException("Session is ended or not available");

		int userId = (int) session.getAttribute("userId");

		personService.withdrawFromCourse(userId, courseId);

		return "redirect:/member/viewEnrolledCourses/1?sortBy=name&sortDir=asc&userId=" + userId;
	}

	@GetMapping("/viewAllCourses/{curPage}")
	public String viewAllCourses(@PathVariable int curPage, @RequestParam int userId, @RequestParam String sortBy,
			@RequestParam String sortDir, HttpSession session, Model model) {

		if (null == session.getAttribute("userId"))
			session.setAttribute("userId", userId);

		Page<Course> courses = courseService.getUnEnrolledCourses(userId, curPage, sortBy, sortDir);

		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("courses", courses.getContent());
		model.addAttribute("pagesCount", courses.getTotalPages());
		model.addAttribute("curPage", curPage);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("userId", userId);

		return "member/view-all-courses";
	}

	@GetMapping("/enroll")
	public String enroll(@RequestParam int courseId, HttpSession session) throws HttpSessionRequiredException {

		if (null == session.getAttribute("userId"))
			throw new HttpSessionRequiredException("Session is ended or not available");

		int userId = (int) session.getAttribute("userId");

		personService.enrollCourse(userId, courseId);

		return "redirect:/member/viewAllCourses/1?sortBy=name&sortDir=asc&userId=" + userId;

	}

}
