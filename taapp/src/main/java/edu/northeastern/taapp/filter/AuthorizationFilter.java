package edu.northeastern.taapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.northeastern.taapp.dao.AdminDAO;
import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Admin;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthorizationFilter implements Filter {
	
	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
	private StaffDAO staffDAO;
	
	@Autowired
	private AdminDAO adminDAO;

    private static List<String> allowedURLs = List.of("/", "/studentRegisterPage", "/studentLoginPage", "/staffRegisterPage", "/staffLoginPage", 
    		"/staffRegister", "/studentRegister");
    private static List<String> studentAllowedURLs = List.of("/application", "/selectCourse", "/selectProfessor", 
    		"/studentEditProfilePage", "/studentViewAllApplications", "/studentViewApplication", "/submitApplicationSuccess", "/viewAllJobs");
    private static List<String> staffAllowedURLs = List.of("/createJobPage", "/createJobSuccess", "/editJobPage", "/rejectApplicationMessage",
    		"/scheduleInterviewMessage", "/selectApplicationMessage", "/viewAllApplications", "/viewApplication", 
    		"/viewJobsPage");
    private static List<String> adminAllowedURLs = List.of("/adminAddCoursePage", "/adminRegisterPage", "/adminViewAllStudents", "/searchStudents",
    		"/viewStudent", "/deleteStudent", "/viewStaff", "/adminViewAllStaffs");
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Object sessionObjStudent = request.getSession().getAttribute("storedStudent");
        Object sessionObjStaff = request.getSession().getAttribute("storedStaff");
        Object sessionObjAdmin = request.getSession().getAttribute("storedAdmin");
        String requestURI = request.getRequestURI();
        System.out.println("Request URI in filter: " + requestURI);
        
        boolean isStudentAllowedURL = false;
        boolean isStaffAllowedURL = false;
        boolean isAdminAllowedURL = false;
        
        for (String allowedURL : studentAllowedURLs) {
            if (requestURI.startsWith(allowedURL)) {
            	isStudentAllowedURL = true;
                break;
            }
        }
        
        for (String allowedURL : staffAllowedURLs) {
            if (requestURI.startsWith(allowedURL)) {
            	isStaffAllowedURL = true;
                break;
            }
        }
        
        for (String allowedURL : adminAllowedURLs) {
            if (requestURI.startsWith(allowedURL)) {
                isAdminAllowedURL = true;
                break;
            }
        }
        
        if(isStudentAllowedURL){
            if(sessionObjStudent != null){
            	Student storedStudent = (Student) sessionObjStudent;
            	String enteredPassword = storedStudent.getPassword();
            	String studentPasswordInDB = studentDAO.getStudentById(storedStudent.getNuid()).getPassword();
            	if (!studentPasswordInDB.equals(enteredPassword)) {
            		System.out.println("In Student filter inner: Redirecting to Home page");
                    response.sendRedirect("/");
                    return;
            	} 
            } else {
                System.out.println("In Student filter outer: Redirecting to Home page");
                response.sendRedirect("/");
                return;
            }
        }
        
        if(isStaffAllowedURL){
            if(sessionObjStaff instanceof Staff){
            	Staff storedStaff = (Staff) sessionObjStaff;
            	String enteredPassword = storedStaff.getPassword();
            	String staffPasswordInDB = staffDAO.getStaffById(storedStaff.getNuid()).getPassword();
            	if (!staffPasswordInDB.equals(enteredPassword)) {
            		System.out.println("In Staff filter inner: Redirecting to Home page");
                    response.sendRedirect("/");
                    return;
            	} 
            } else {
                System.out.println("In Staff filter outer: Redirecting to Home page");
                response.sendRedirect("/");
                return;
            }
        }
        
        if(isAdminAllowedURL){
            if(sessionObjAdmin instanceof Admin){
            	Admin storedAdmin = (Admin) sessionObjAdmin;
            	String enteredPassword = storedAdmin.getPassword();
            	String adminPasswordInDB = adminDAO.getAdminById(storedAdmin.getAdminId()).getPassword();
            	if (!adminPasswordInDB.equals(enteredPassword)) {
            		System.out.println("In Admin filter inner: Redirecting to Home page");
                    response.sendRedirect("/");
                    return;
            	} 
            } else {
                System.out.println("In Admin filter outer: Redirecting to Home page");
                response.sendRedirect("/");
                return;
            }
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

