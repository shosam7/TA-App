package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class JobController {
	
	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private StaffDAO staffDAO;
	
	@Autowired
	private CourseDAO courseDAO;
	
	@PostMapping("/createJob")
	public String createJob(Model model, Job job, HttpServletRequest httprequest, BindingResult bindingResult,
			@RequestParam String courseId) {
		
		try {
			Object sessionObj = httprequest.getSession().getAttribute("storedStaff");
			if(sessionObj instanceof Staff) {
				Staff storedStaff = (Staff) sessionObj;
				Staff staff = staffDAO.getStaffById(storedStaff.getNuid());
				Course course = courseDAO.getCourseById(courseId);
				if(jobDAO.getJobByStaffAndCourse(staff, course) != null) {
					bindingResult.rejectValue("course", "error.job", "Job already exists, make changes in Edit Job");
				}
				if (bindingResult.hasErrors()) {
					List<Course> courses = courseDAO.getAllCourses();
					model.addAttribute("courses", courses);
			        return "createJobPage";
			    }
				job.setStaff(staff);
				job.setCourse(course);
				model.addAttribute("job", job);
				jobDAO.saveJob(job);
			} else {
				return "redirect:/";
			}
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("unique constraint")) {
            	bindingResult.rejectValue("course", "error.job", "A job with the given course already exists.");
            	return "createJobPage";
            }
        }
		return "createJobSuccess";
	}
}
