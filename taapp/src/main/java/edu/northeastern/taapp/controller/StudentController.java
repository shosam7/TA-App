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
import edu.northeastern.taapp.util.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class StudentController {

	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/studentRegister")
	public String studentRegister(@Valid @ModelAttribute("student") Student student,BindingResult bindingResult, Model studentModel) {
		
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
	        if (!FileUploadUtil.isPDF(transcript)) {
	        	bindingResult.rejectValue("transcript", "error.student", "Transcript must be a PDF file");
	            return "studentRegisterPage";
	        }
	        String transcriptPath = FileUploadUtil.saveFile(transcript, student.getNuid(), "Transcript");
	        student.setTranscriptPath(transcriptPath);
	    }

	    if (photo != null && !photo.isEmpty()) {
	        if (!FileUploadUtil.isJPEG(photo)) {
	        	bindingResult.rejectValue("photo", "error.student", "Photo must be a Jpg file");
	            return "studentRegisterPage";
	        }
	        String photoPath = FileUploadUtil.saveFile(photo, student.getNuid(), "Photo");
	        student.setPhotoPath(photoPath);
	    }

	    String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);
	    
		try {
			studentDAO.saveStudent(student);
		} catch(Exception e) {
			return "studentRegisterFail";
		}
		
		studentModel.addAttribute("student", student);
		return "studentRegisterSuccess";
	}
	
	@PostMapping("/studentDashboard")
    public String studentDashboard(Student student, Errors errors, Model model, HttpSession httpSession) {
        String nuid = student.getNuid();
        String enteredPassword = student.getPassword();

        Student storedStudent = studentDAO.getStudentById(nuid);

        if (storedStudent != null) {
            if (passwordEncoder.matches(enteredPassword, storedStudent.getPassword())) {
            	httpSession.setAttribute("storedStudent", storedStudent);
                model.addAttribute("student", storedStudent);
                return "studentDashboard";
            }
        }
        
        errors.rejectValue("password", "error.student", "Invalid NUID or password");
        return "studentLoginPage";
    }
	
	@GetMapping("/studentDashboard")
	public String studentDashboard(HttpSession httpSession, Model model) {
		Object sessionObj = httpSession.getAttribute("storedStudent");
		if(sessionObj instanceof Student) {
			Student storedStudent = (Student) sessionObj;
			model.addAttribute("student", storedStudent);
			return "studentDashboard";
		}
		return "redirect:/";
	}
	
	@GetMapping("/studentLogout")
    public String studentLogout(HttpSession session) {
		session.invalidate();
        return "redirect:/";
    }
}
