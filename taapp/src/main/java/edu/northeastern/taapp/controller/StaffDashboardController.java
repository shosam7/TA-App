package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller

public class StaffDashboardController {
	
	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private JobDAO jobDAO;
	
	@GetMapping("/createJobPage")
	public String createJobPage(Model model, Job job) {
		List<Course> courses = courseDAO.getAllCourses();
		model.addAttribute("job", job);
		model.addAttribute("courses", courses);
		return "createJobPage";
	}
	
	@GetMapping("/viewJobsPage")
	public String viewJobsPage(Model model, HttpServletRequest httpRequest) {
		Staff storedStaff = (Staff) httpRequest.getSession().getAttribute("storedStaff");
		List<Job> jobsByStaff = jobDAO.getJobsByStaff(storedStaff);
		model.addAttribute("jobs", jobsByStaff);
		return "viewJobsPage";
	}
}
