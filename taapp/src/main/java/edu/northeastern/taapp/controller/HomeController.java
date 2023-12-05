package edu.northeastern.taapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;


@Controller
public class HomeController {
	
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
