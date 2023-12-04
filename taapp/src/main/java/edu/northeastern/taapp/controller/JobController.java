package edu.northeastern.taapp.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;

import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;

@Controller
public class JobController {
	
	@PostMapping("/createJob")
	public String createJob(Model model, Job job, Staff staff, Errors errors) {
		
		try {
            model.addAttribute("job", job);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("unique constraint")) {
            	errors.rejectValue("courseName", "error.job", "A job with the given course already exists.");
            	return "createJobPage";
            }
        }
		return "createJobSuccess";
	}
	
}
