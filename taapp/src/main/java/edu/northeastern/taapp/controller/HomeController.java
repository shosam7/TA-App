package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;
import edu.northeastern.taapp.model.Course;

import java.util.List;

@Controller
public class HomeController {
	
	@Autowired
    private CourseDAO courseDAO;
	
	@GetMapping("/")
	public String homeHandler() {
		return "index";
	}
	
	@GetMapping("/studentRegisterPage")
	public String studentRegisterHandler(Model studentModel, Student student) {
		studentModel.addAttribute("student", student);
		return "studentRegisterPage";
	}
	
	@GetMapping("/studentLoginPage")
	public String studentLoginHandler(Model model, Student student) {
		return "studentLoginPage";
	}
	
	@GetMapping("/staffRegisterPage")
	public String staffRegisterHandler(Model model, Staff staff) {
		model.addAttribute("staff", staff);
		return "staffRegisterPage";
	}
	
	@GetMapping("/staffLoginPage")
	public String staffLoginHandler(Staff staff) {
		return "staffLoginPage";
	}
}
