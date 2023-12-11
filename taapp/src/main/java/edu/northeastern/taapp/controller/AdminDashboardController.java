package edu.northeastern.taapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.model.Course;

@Controller
public class AdminDashboardController {
	
	@GetMapping("/adminAddCoursePage")
	public String addCoursePage(Course course, Model model) {
		model.addAttribute("course", course);
		return "adminAddCoursePage";
	}
}
