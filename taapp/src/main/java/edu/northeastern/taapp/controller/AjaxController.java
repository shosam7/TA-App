package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Application.Status;
import edu.northeastern.taapp.model.Student;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AjaxController {
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
	@GetMapping("/groupApplications")
	public ResponseEntity<List<Application>> viewApplications(@RequestParam String status, HttpServletRequest httpRequest) {
		Student student = (Student) httpRequest.getSession().getAttribute("storedStudent");
		if("ALL".equalsIgnoreCase(status.toString())) {
			List<Application> applications = applicationDAO.getApplicationsByStudent(student);
			return new ResponseEntity<>(applications, HttpStatus.OK);
		}
		List<Application> applications = applicationDAO.getApplicationsByStudentAndStatus(student, Status.valueOf(status));
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}
}
