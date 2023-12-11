package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.model.Course;

@Controller
public class CourseController {
	
	@Autowired
	private CourseDAO courseDAO;
	
	@PostMapping("/adminAddCourse")
	public String addCourse(@ModelAttribute Course course, BindingResult bindingResult, Model model) {
		
		Course storedCourse = courseDAO.getCourseById(course.getCourseId());
		
		if(storedCourse != null) {
			bindingResult.rejectValue("courseId", "error.course", "Course with this id already exists");
			model.addAttribute("course", course);
		}
		
		if(bindingResult.hasErrors()) {
			return "adminAddCoursePage";
		}
		model.addAttribute("course", course);
		model.addAttribute("successMessage", "Course added successfully!");
		courseDAO.saveCourse(course);
		return "adminAddCoursePage";
	}
}
