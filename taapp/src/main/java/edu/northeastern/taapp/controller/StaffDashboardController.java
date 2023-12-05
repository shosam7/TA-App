package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;

import java.util.List;

@Controller

public class StaffDashboardController {
	
	@Autowired
	private CourseDAO courseDAO;
	
	@GetMapping("/createJobPage")
	public String createJobPage(Model model, Job job) {
		List<Course> courses = courseDAO.getAllCourses();
		model.addAttribute("job", job);
		model.addAttribute("courses", courses);
		return "createJobPage";
	}
}
