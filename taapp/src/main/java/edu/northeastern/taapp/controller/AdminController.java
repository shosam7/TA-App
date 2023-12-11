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

import edu.northeastern.taapp.dao.AdminDAO;
import edu.northeastern.taapp.model.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AdminController {
	
	@Autowired
	private AdminDAO adminDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
	
	@GetMapping("/adminLogout")
    public String adminLogout(HttpServletRequest httpRequest) {
		httpRequest.getSession().invalidate();
        return "redirect:/";
    }
}
