package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.model.Job;

@Controller
public class StudentDashboardController {
	
	@Autowired
	private JobDAO jobDAO;
	
	@GetMapping("/viewAllJobs")
    public String viewAllJobs(Model model) {
        List<Job> jobs = jobDAO.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "viewAllJobs";
    }
}
