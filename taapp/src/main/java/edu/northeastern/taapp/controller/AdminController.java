package edu.northeastern.taapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.northeastern.taapp.dao.AdminDAO;
import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.dao.JobDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Admin;
import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;
import edu.northeastern.taapp.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AdminController {

	@Autowired
	private AdminDAO adminDAO;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private StaffDAO staffDAO;

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationDAO applicationDAO;

	@PostMapping("/adminRegister")
	public String staffRegister(@Valid @ModelAttribute("admin") Admin admin, BindingResult bindingResult, Model model) {

		Admin existingAdminById = adminDAO.getAdminById(admin.getAdminId());
		if (existingAdminById != null) {
			bindingResult.rejectValue("nuid", "error.staff", "Admin ID already exists");
		}

		if (bindingResult.hasErrors()) {
			return "adminRegisterPage";
		}

		String hashedPassword = passwordEncoder.encode(admin.getPassword());
		admin.setPassword(hashedPassword);

		try {
			adminDAO.saveAdmin(admin);
		} catch (Exception e) {
			return "staffRegisterFail";
		}

		model.addAttribute("admin", admin);
		return "redirect:/";
	}

	@PostMapping("/adminDashboard")
	public String adminDashboard(Admin admin, Errors errors, Model model, HttpServletRequest httprequest) {
		String adminId = admin.getAdminId();
		String enteredPassword = admin.getPassword();

		Admin storedAdmin = adminDAO.getAdminById(adminId);

		if (storedAdmin != null) {
			if (passwordEncoder.matches(enteredPassword, storedAdmin.getPassword())) {
				httprequest.getSession().setAttribute("storedAdmin", storedAdmin);
				model.addAttribute("admin", storedAdmin);
				return "adminDashboard";
			}
		}

		errors.rejectValue("password", "error.admin", "Invalid Admin ID or password");
		return "adminLoginPage";
	}

	@GetMapping("/adminDashboard")
	public String adminDashboard(Model model, HttpServletRequest httprequest) {
		Admin storedAdmin = (Admin) httprequest.getSession().getAttribute("storedAdmin");
		if (storedAdmin != null) {
			model.addAttribute("admin", storedAdmin);
			return "adminDashboard";
		}
		return "adminLoginPage";
	}

	@GetMapping("/adminLogout")
	public String adminLogout(HttpServletRequest httpRequest) {
		httpRequest.getSession().invalidate();
		return "redirect:/";
	}

	@GetMapping("/searchStudents")
	public String searchStudents(Model model, HttpServletRequest httpRequest) {
		String keyword = (String) httpRequest.getParameter("keyword");
		List<Student> studentsByKeyword = studentDAO.getStudentsByKeyword(keyword);
		model.addAttribute("students", studentsByKeyword);
		return "adminViewAllStudents";
	}

	@GetMapping("/viewStudent/{studentNuid}")
	public String viewStudent(Model model, @PathVariable String studentNuid) {
		Student student = studentDAO.getStudentById(studentNuid);
		model.addAttribute("student", student);
		return "viewStudent";
	}

	@GetMapping("/deleteStudent/{studentNuid}")
	public String deleteStudent(Model model, @PathVariable String studentNuid) {
		Student student = studentDAO.getStudentById(studentNuid);
		System.out.println(student.getTranscriptPath() + " " + student.getPhotoPath());
		boolean isTranscriptDeleted = FileUtil.deleteFile(student.getTranscriptPath());
		boolean isPhotoDeleted = FileUtil.deleteFile(student.getPhotoPath());
		List<Application> applicationsByStudent = applicationDAO.getApplicationsByStudent(student);
		boolean isResumeDeleted = true;
		if (applicationsByStudent != null) {
			isResumeDeleted = FileUtil.deleteFile(applicationsByStudent.get(0).getResumePath());
		}

		if (isTranscriptDeleted && isPhotoDeleted && isResumeDeleted) {
			for (Application application : applicationsByStudent) {
				applicationDAO.deleteApplication(application.getApplicationId());
			}
			studentDAO.deleteStudent(studentNuid);
		}

		return "redirect:/adminViewAllStudents";
	}

	@GetMapping("/searchStaffs")
	public String searchStaffs(Model model, HttpServletRequest httpRequest) {
		String keyword = (String) httpRequest.getParameter("keyword");
		List<Staff> staffsByKeyword = staffDAO.getStaffsByKeyword(keyword);
		model.addAttribute("staffs", staffsByKeyword);
		return "adminViewAllStaffs";
	}

	@GetMapping("/viewStaff/{staffNuid}")
	public String viewStaff(Model model, @PathVariable String staffNuid) {
		Staff staff = staffDAO.getStaffById(staffNuid);
		model.addAttribute("staff", staff);
		return "viewStaff";
	}

	@GetMapping("/deleteStaff/{staffNuid}")
	public String deleteStaff(Model model, @PathVariable String staffNuid) {
		Staff staff = staffDAO.getStaffById(staffNuid);

		List<Application> applicationsByStaff = applicationDAO.getApplicationsByStaff(staff);
		for (Application application : applicationsByStaff) {
			applicationDAO.deleteApplication(application.getApplicationId());
		}

		List<Job> jobsByStaff = jobDAO.getJobsByStaff(staff);
		for (Job job : jobsByStaff) {
			jobDAO.deleteJob(job.getJobId());
		}

		staffDAO.deleteStaff(staffNuid);

		model.addAttribute("staff", staff);
		return "redirect:/adminViewAllStaffs";
	}
}
