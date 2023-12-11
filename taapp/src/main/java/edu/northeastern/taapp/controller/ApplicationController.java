package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.dao.CourseDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Application.Status;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;
import edu.northeastern.taapp.service.EmailService;
import edu.northeastern.taapp.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ApplicationController {

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private StaffDAO staffDAO;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private ApplicationDAO applicationDAO;

	@Autowired
	private EmailService emailService;

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
		model.addAttribute("jobs", jobsByStaff);
		return "selectCourse";
	}

	@PostMapping("/apply/application")
	public String viewApplication(Model model, Application application, @RequestParam String selectedJob) {
		Job job = jobDAO.getJobById(selectedJob);
		model.addAttribute("job", job);
		model.addAttribute("application", application);
		return "application";
	}

	@PostMapping("/apply/submitApplication")
	public String submitApplication(Model model, @ModelAttribute("application") Application application,
			@RequestParam String jobId, HttpServletRequest httpRequest, BindingResult bindingResult) {
		Student storedStudent = (Student) httpRequest.getSession().getAttribute("storedStudent");
		Student student = studentDAO.getStudentById(storedStudent.getNuid());

		Job job = jobDAO.getJobById(jobId);

		if (applicationDAO.getApplicationByStudentAndJob(student, job) != null) {
			bindingResult.rejectValue("job", "error.application",
					"Application already exists, delete application to apply again");
		}

		MultipartFile resume = application.getResume();
		if (resume != null && !resume.isEmpty()) {
			if (!FileUploadUtil.isPDF(resume)) {
				bindingResult.rejectValue("resume", "error.application", "Resume must be a PDF file");
			}
			String resumePath = FileUploadUtil.saveFile(resume, student.getNuid(), "Resume");
			application.setResumePath(resumePath);
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute(job);
			return "application";
		}

		application.setStudent(student);
		application.setJob(job);
		Staff staff = staffDAO.getStaffById(job.getStaff().getNuid());
		application.setStaff(staff);
		application.setStatus(Status.ACTIVE);

		applicationDAO.saveApplication(application);
		model.addAttribute("fullName", job.getStaff().getFullName());
		model.addAttribute("courseName", job.getCourse().getCourseName());
		return "submitApplicationSuccess";
	}

//	@GetMapping("/viewAllApplications")
//	public String viewApplicationsByStaff(Model model, HttpServletRequest httpRequest, @RequestParam(name = "page", defaultValue = "0") int page) {
//	    Staff staff = (Staff) httpRequest.getSession().getAttribute("storedStaff");
//	    
//	    Pageable pageable = PageRequest.of(page, 5);
//	    Page<Application> taApplicationsPage = applicationDAO.getApplicationsByStaff(staff, pageable);
//
//	    model.addAttribute("taApplications", taApplicationsPage.getContent());
//	    model.addAttribute("page", taApplicationsPage);
//
//	    return "viewAllApplications";
//	}

	@GetMapping("/viewAllApplications")
	public String viewAllApplications(Model model, HttpServletRequest httpRequest,
			@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "0") String jobId,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		Staff staff = (Staff) httpRequest.getSession().getAttribute("storedStaff");
		Pageable pageable = PageRequest.of(page, 5);
		Page<Application> taApplicationsPage = null;
		
		if (!status.equals("") && !jobId.equals("0")) {
			Job job = jobDAO.getJobById(jobId);
			taApplicationsPage = applicationDAO.getFilteredApplications(staff, status, job, pageable);
			model.addAttribute("courseName", job.getCourse().getCourseName());
		} else if(!status.equals("") && jobId.equals("0")) {
			taApplicationsPage = applicationDAO.getApplicationsByStaffAndStatus(staff, status, pageable);
		} else if(status.equals("") && !jobId.equals("0")) {
			Job job = jobDAO.getJobById(jobId);
			taApplicationsPage = applicationDAO.getApplicationsByStaffAndJob(staff, job, pageable);
			model.addAttribute("courseName", job.getCourse().getCourseName());
		} else {
			taApplicationsPage = applicationDAO.getApplicationsByStaff(staff, pageable);
		}

		List<Job> jobs = jobDAO.getJobsByStaff(staff);
		
		model.addAttribute("jobs", jobs);
		model.addAttribute("taApplications", taApplicationsPage.getContent());
		model.addAttribute("page", taApplicationsPage);
		model.addAttribute("status", status);
		model.addAttribute("jobId", jobId);

		return "viewAllApplications";
	}

	@GetMapping("/viewApplication/{applicationId}")
	public String viewApplication(Model model, @PathVariable Long applicationId, HttpServletRequest httpRequest) {
		Staff storedStaff = (Staff) httpRequest.getSession().getAttribute("storedStaff");
		if (storedStaff != null) {
			Application application = applicationDAO.getApplicationById(applicationId);
			model.addAttribute("taApplication", application);
			return "viewApplication";
		}
		return "redirect:/";
	}

	@GetMapping("/rejectApplicationMessage/{applicationId}")
	public String rejectApplicationMessage(Model model, @PathVariable Long applicationId) {
		Application application = applicationDAO.getApplicationById(applicationId);
		String rejectionMessage = "Unfortunately you have not been selected for this position:\nProfessor: "
				+ application.getStaff().getFullName() + "\nCourse: "
				+ application.getJob().getCourse().getCourseName();
		model.addAttribute("taApplication", application);
		model.addAttribute("rejectionMessage", rejectionMessage);
		return "rejectApplicationMessage";
	}

	@PostMapping("/rejectApplication")
	public String rejectApplication(Model model, @RequestParam Long applicationId,
			@RequestParam String rejectionMessage) {
		Application application = (Application) applicationDAO.getApplicationById(applicationId);
		application.setMessage(rejectionMessage);
		application.setStatus(Status.INACTIVE);
		applicationDAO.updateApplication(application);
		emailService.sendSimpleMessage(application.getStudent().getEmail(), "Update on your TA application",
				rejectionMessage);
		return "redirect:/viewAllApplications";
	}

	@GetMapping("/scheduleInterviewMessage/{applicationId}")
	public String scheduleInterviewMessage(Model model, @PathVariable Long applicationId) {
		Application application = applicationDAO.getApplicationById(applicationId);
		String interviewMessage = "Enter details of interview for this position:\nProfessor: "
				+ application.getStaff().getFullName() + "\nCourse: "
				+ application.getJob().getCourse().getCourseName();
		model.addAttribute("taApplication", application);
		model.addAttribute("interviewMessage", interviewMessage);
		return "scheduleInterviewMessage";
	}

	@PostMapping("/scheduleInterview")
	public String scheduleInterview(Model model, @RequestParam Long applicationId,
			@RequestParam String interviewMessage) {
		Application application = (Application) applicationDAO.getApplicationById(applicationId);
		application.setMessage(interviewMessage);
		application.setStatus(Status.ACTIVE);
		applicationDAO.updateApplication(application);
		emailService.sendSimpleMessage(application.getStudent().getEmail(), "Update on your TA application",
				interviewMessage);
		return "redirect:/viewAllApplications";
	}

	@GetMapping("/selectApplicationMessage/{applicationId}")
	public String selectApplicationMessage(Model model, @PathVariable Long applicationId) {
		Application application = applicationDAO.getApplicationById(applicationId);
		Job job = application.getJob();
		job.setNumOpenings(job.getNumOpenings() - 1);
		jobDAO.updateJob(job);
		String selectedMessage = "Congratulations you have been selected for this position as TA:\nProfessor: "
				+ application.getStaff().getFullName() + "\nCourse: "
				+ application.getJob().getCourse().getCourseName();
		model.addAttribute("taApplication", application);
		model.addAttribute("selectedMessage", selectedMessage);
		return "selectApplicationMessage";
	}

	@PostMapping("/selectApplication")
	public String selectApplication(Model model, @RequestParam Long applicationId,
			@RequestParam String selectedMessage) {
		Application application = (Application) applicationDAO.getApplicationById(applicationId);
		application.setMessage(selectedMessage);
		application.setStatus(Status.SELECTED);
		applicationDAO.updateApplication(application);
		emailService.sendSimpleMessage(application.getStudent().getEmail(), "Update on your TA application",
				selectedMessage);
		return "redirect:/viewAllApplications";
	}

	@GetMapping("/studentViewApplication/{applicationId}")
	public String studentViewApplication(Model model, @PathVariable Long applicationId,
			HttpServletRequest httpRequest) {
		Student storedStudent = (Student) httpRequest.getSession().getAttribute("storedStudent");
		if (storedStudent == null) {
			return "redirect:/";
		}

		Application application = applicationDAO.getApplicationById(applicationId);
		if (!application.getStudent().getNuid().equals(storedStudent.getNuid())) {
			return "redirect:/";
		}
		model.addAttribute("taApplication", application);
		return "studentViewApplication";
	}

	@GetMapping("/deleteApplicationStudent/{applicationId}")
	public String deleteApplicationStudent(Model model, @PathVariable Long applicationId,
			HttpServletRequest httpRequest) {
		Student storedStudent = (Student) httpRequest.getSession().getAttribute("storedStudent");

		if (storedStudent == null) {
			return "redirect:/";
		}
		Application application = applicationDAO.getApplicationById(applicationId);

		if (!application.getStudent().getNuid().equals(storedStudent.getNuid())) {
			return "redirect:/";
		}
		applicationDAO.deleteApplication(applicationId);
		return "redirect:/studentViewAllApplications";
	}
}
