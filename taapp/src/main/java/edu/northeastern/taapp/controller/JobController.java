package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.model.Application;
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
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
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
	
	@GetMapping("editJobPage/{jobId}")
	public String updateJob(Model model, @PathVariable Long jobId) {
		Job job = jobDAO.getJobById(jobId.toString());
		model.addAttribute("job", job);
		return "editJobPage";
	}
	
	@PostMapping("/updateJob")
	public String updateJob(@ModelAttribute Job job) {
		Job existingJob = jobDAO.getJobById(job.getJobId().toString());
		job.setCourse(existingJob.getCourse());
		job.setStaff(existingJob.getStaff());
		jobDAO.updateJob(job);
		return"redirect:/viewJobsPage";
	}
	
	@GetMapping("/deleteJob/{jobId}")
	public String deleteJob(@PathVariable Long jobId, HttpServletRequest httpRequest) {
		Staff storedStaff = (Staff) httpRequest.getSession().getAttribute("storedStaff");
		Job job = jobDAO.getJobById(jobId.toString());
		if(job.getStaff().getNuid().equals(storedStaff.getNuid())) {
			List<Application> applicationsByJob = applicationDAO.getApplicationsByJob(job);
			for(Application application : applicationsByJob) {
				applicationDAO.deleteApplication(application.getApplicationId());
			}
			jobDAO.deleteJob(jobId);
		}
		return "redirect:/viewJobsPage";
	}
}
