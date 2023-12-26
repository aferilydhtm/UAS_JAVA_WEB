package com.uas.course.controllers.adminControllers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uas.course.models.Person;
import com.uas.course.services.servicesImp.PersonServiceImp;

@Controller
@RequestMapping("/admin")
public class AdminMembersController {

	private final PersonServiceImp personService;

	public AdminMembersController(PersonServiceImp personService) {
		this.personService = personService;
	}

	@GetMapping("/viewAllMembers/{curPage}")
	public String viewMembers(@PathVariable int curPage, @RequestParam String sortBy, @RequestParam String sortDir,
			Model model) {

		Page<Person> members = personService.getAllMembers(curPage, sortBy, sortDir);
		sortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("members", members.getContent());
		model.addAttribute("pagesCount", members.getTotalPages());
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("curPage", curPage);

		return "admin/view-all-members";
	}

	@GetMapping("/memberDetails")
	public String getMemberDetails(@RequestParam(required = true) int memberId, Model model) {
		Person member = personService.getPerson(memberId);
		model.addAttribute("member", member);
		return "admin/view-member-details";
	}

	@GetMapping("/deleteMember")
	public String deleteMember(@RequestParam(required = true) int memberId) {

		personService.deleteMember(memberId);

		return "redirect:/admin/viewAllMembers/1?sortBy=name&sortDir=asc";
	}

}
