package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Student;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StudentDashboardController {
	
	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
	@GetMapping("/viewAllJobs")
    public String viewAllJobs(Model model) {
        List<Job> jobs = jobDAO.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "viewAllJobs";
    }
	
	@GetMapping("/studentViewAllApplications")
	public String studentViewAllApplications(Model model, HttpServletRequest httpRequest) {
		Student student = (Student) httpRequest.getSession().getAttribute("storedStudent");
		List<Application> studentApplications = applicationDAO.getApplicationsByStudent(student);
		model.addAttribute("studentApplications", studentApplications);
		return "studentViewAllApplications";
	}
}
