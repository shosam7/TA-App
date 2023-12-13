package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

@Controller
public class AdminDashboardController {
	
	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
	private StaffDAO staffDAO;
	
	@GetMapping("/adminAddCoursePage")
	public String addCoursePage(Course course, Model model) {
		model.addAttribute("course", course);
		return "adminAddCoursePage";
	}
	
	@GetMapping("/adminViewAllStudents")
	public String viewAllStudents(Model model) {
		List<Student> allStudents = studentDAO.getAllStudents();
		model.addAttribute("students", allStudents);
		return "adminViewAllStudents";
	}
	
	@GetMapping("/adminViewAllStaffs")
	public String viewAllStaffs(Model model) {
		List<Staff> allStaffs = staffDAO.getAllStaffs();
		model.addAttribute("staffs", allStaffs);
		return "adminViewAllStaffs";
	}
}
