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

import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.model.Staff;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class StaffController {

	@Autowired
	private StaffDAO staffDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/staffRegister")
	public String staffRegister(@Valid @ModelAttribute("staff") Staff staff, BindingResult bindingResult, Model model) {

		Staff existingStaffById = staffDAO.getStaffById(staff.getNuid());
		Staff existingStaffByEmail = staffDAO.getStaffByEmail(staff.getEmail());
		if (existingStaffById != null) {
			bindingResult.rejectValue("nuid", "error.staff", "NUID already exists");
		}

		if (existingStaffByEmail != null) {
			bindingResult.rejectValue("email", "error.staff", "Email already exists");
		}

		if (bindingResult.hasErrors()) {
			return "staffRegisterPage";
		}

		String hashedPassword = passwordEncoder.encode(staff.getPassword());
		staff.setPassword(hashedPassword);

		try {
			staffDAO.saveStaff(staff);
		} catch (Exception e) {
			return "staffRegisterFail";
		}

		model.addAttribute("staff", staff);
		return "staffRegisterSuccess";
	}

	@PostMapping("/staffLogin")
	public String studentLogin(Staff staff, Errors errors, Model model) {
		String nuid = staff.getNuid();
		String enteredPassword = staff.getPassword();

		Staff storedStaff = staffDAO.getStaffById(nuid);

		if (storedStaff != null) {
			if (passwordEncoder.matches(enteredPassword, storedStaff.getPassword())) {
				model.addAttribute("staff", storedStaff);
				return "staffDashboard";
			}
		}

		errors.rejectValue("password", "error.staff", "Invalid NUID or password");
		return "staffLoginPage";
	}

	@GetMapping("/staffLogout")
    public String staffLogout(HttpSession session) {
        return "redirect:/";
    }
}
