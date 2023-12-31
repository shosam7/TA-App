package edu.northeastern.taapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Student;
import edu.northeastern.taapp.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class StudentController {

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/studentRegister")
	public String studentRegister(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult,
			Model studentModel) {

		Student existingStudentById = studentDAO.getStudentById(student.getNuid());
		Student existingStudentByEmail = studentDAO.getStudentByEmail(student.getEmail());
		if (existingStudentById != null) {
			bindingResult.rejectValue("nuid", "error.student", "NUID already exists");
		}

		if (existingStudentByEmail != null) {
			bindingResult.rejectValue("email", "error.student", "Email already exists");
		}

		if (bindingResult.hasErrors()) {
			return "studentRegisterPage";
		}

		MultipartFile transcript = student.getTranscript();
		MultipartFile photo = student.getPhoto();

		if (transcript != null && !transcript.isEmpty()) {
			if (!FileUtil.isPDF(transcript)) {
				bindingResult.rejectValue("transcript", "error.student", "Transcript must be a PDF file");
				return "studentRegisterPage";
			}
			String transcriptPath = FileUtil.saveFile(transcript, student.getNuid(), "Transcript");
			student.setTranscriptPath(transcriptPath);
		}

		if (photo != null && !photo.isEmpty()) {
			if (!FileUtil.isJPEG(photo)) {
				bindingResult.rejectValue("photo", "error.student", "Photo must be a Jpg file");
				return "studentRegisterPage";
			}
			String photoPath = FileUtil.saveFile(photo, student.getNuid(), "Photo");
			student.setPhotoPath(photoPath);
		}

		String hashedPassword = passwordEncoder.encode(student.getPassword());
		student.setPassword(hashedPassword);

		try {
			studentDAO.saveStudent(student);
		} catch (Exception e) {
			return "studentRegisterFail";
		}

		studentModel.addAttribute("student", student);
		return "studentRegisterSuccess";
	}

	@PostMapping("/studentDashboard")
	public String studentDashboard(Student student, Errors errors, Model model, HttpServletRequest httpRequest) {
		String nuid = student.getNuid();
		String enteredPassword = student.getPassword();

		Student storedStudent = studentDAO.getStudentById(nuid);

		if (storedStudent != null) {
			if (passwordEncoder.matches(enteredPassword, storedStudent.getPassword())) {
				httpRequest.getSession().setAttribute("storedStudent", storedStudent);
				model.addAttribute("student", storedStudent);
				return "studentDashboard";
			}
		}

		errors.rejectValue("password", "error.student", "Invalid NUID or password");
		return "studentLoginPage";
	}

	@GetMapping("/studentDashboard")
	public String studentDashboard(HttpServletRequest httpRequest, Model model) {
		Object sessionObj = httpRequest.getSession().getAttribute("storedStudent");
		if (sessionObj instanceof Student) {
			Student storedStudent = (Student) sessionObj;
			model.addAttribute("student", storedStudent);
			return "studentDashboard";
		}
		return "redirect:/";
	}

	@GetMapping("/studentLogout")
	public String studentLogout(HttpServletRequest httpRequest) {
		httpRequest.getSession().invalidate();
		return "redirect:/";
	}

	@GetMapping("/studentEditProfilePage")
	public String editProfile(Model model, HttpServletRequest httpRequest) {
		Student storedStudent = (Student) httpRequest.getSession().getAttribute("storedStudent");
		model.addAttribute("student", storedStudent);
		return "studentEditProfilePage";
	}

	@PostMapping("/updateStudent")
	public String updateStudent(@ModelAttribute Student student, HttpServletRequest httpRequest,
			BindingResult bindingResult) {
		Student storedStudent = (Student) httpRequest.getSession().getAttribute("storedStudent");
		
		student.setNuid(storedStudent.getNuid());
		student.setEmail(storedStudent.getEmail());
		student.setPassword(storedStudent.getPassword());
		
		MultipartFile transcript = student.getTranscript();
		MultipartFile photo = student.getPhoto();

		if (transcript != null && !transcript.isEmpty()) {
			if (!FileUtil.isPDF(transcript)) {
				bindingResult.rejectValue("transcript", "error.student", "Transcript must be a PDF file");
			} else {
				String transcriptPath = FileUtil.saveFile(transcript, student.getNuid(), "Transcript");
				student.setTranscriptPath(transcriptPath);
			}
		} else {
			student.setTranscriptPath(storedStudent.getTranscriptPath());
		}

		if (photo != null && !photo.isEmpty()) {
			if (!FileUtil.isJPEG(photo)) {
				bindingResult.rejectValue("photo", "error.student", "Photo must be a Jpg file");
			} else {
				String photoPath = FileUtil.saveFile(photo, student.getNuid(), "Photo");
				student.setPhotoPath(photoPath);
			}
		} else {
			student.setPhotoPath(storedStudent.getPhotoPath());
		}

		if (bindingResult.hasErrors()) {
			return "studentEditProfilePage";
		}
		studentDAO.updateStudent(student);
		httpRequest.getSession().setAttribute("storedStudent", student);
		return "redirect:/studentDashboard";
	}
}
