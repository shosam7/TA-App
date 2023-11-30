package edu.northeastern.taapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String homeHandler() {
		return "index";
	}
	
	@GetMapping("/new-student")
	public String newStudentHandler() {
		return "student-register";
	}
	
	@GetMapping("/student")
	public String studentLoginHandler() {
		return "student-login";
	}
	
	@GetMapping("/new-professor")
	public String newProfessorHandler() {
		return "professor-register";
	}
	
	@GetMapping("/professor")
	public String professorLoginHandler() {
		return "professor-login";
	}
}
