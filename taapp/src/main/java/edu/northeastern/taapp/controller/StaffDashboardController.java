package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Staff;

import java.util.List;

@Controller

public class StaffDashboardController {
	
	@Autowired
	private CourseDAO courseDAO;
	
	@GetMapping("/createJobPage")
	public String createJob(Model model, Staff staff) {
		List<Course> courses = courseDAO.getAllCourses();
		model.addAttribute("courses", courses);
		model.addAttribute("staff", staff);
		return "createJobPage";
	}
}
