package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ApplicationController {
	
	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private StaffDAO staffDAO;
	
	@GetMapping("/apply/selectProfessor")
    public String applySelectProfessor(Model model) {
		List<Staff> uniqueStaffList = jobDAO.getUniqueStaffList();
        model.addAttribute("uniqueStaffList", uniqueStaffList);
        return "selectProfessor";
    }
	
	@PostMapping("/apply/selectCourse")
    public String applySelectCourse(Model model, HttpServletRequest httpRequest) {
		String staffNuid = (String) httpRequest.getParameter("selectedStaff");
		System.out.println(staffNuid);
		Staff selectedStaff = staffDAO.getStaffById(staffNuid);
		List<Job> jobsByStaff = jobDAO.getJobsByStaff(selectedStaff);
		model.addAttribute("selectedStaff", selectedStaff);
        model.addAttribute("jobsByStaff", jobsByStaff);
        return "selectCourse";
    }
}
