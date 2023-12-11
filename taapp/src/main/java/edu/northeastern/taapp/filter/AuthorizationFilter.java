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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import edu.northeastern.taapp.dao.StaffDAO;
import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthorizationFilter implements Filter {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private StudentDAO studentDAO;
	
	@Autowired
	private StaffDAO staffDAO;

    private static List<String> allowedURLs = List.of("/", "/studentRegisterPage", "/studentLoginPage", "/staffRegisterPage", "/staffLoginPage", 
    		"/staffRegister", "/studentRegister");
    private static List<String> studentAllowedURLs = List.of("/application", "/selectCourse", "/selectProfessor", 
    		"/studentEditProfilePage", "/studentViewAllApplications", "/studentViewApplication", "/submitApplicationSuccess", "/viewAllJobs");
    private static List<String> staffAllowedURLs = List.of("/createJobPage", "/createJobSuccess", "/editJobPage", "/rejectApplicationMessage",
    		"/scheduleInterviewMessage", "/selectApplicationMessage", "/viewAllApplications", "/viewApplication", 
    		"/viewJobsPage");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Object sessionObjStudent = request.getSession().getAttribute("storedStudent");
        Object sessionObjStaff = request.getSession().getAttribute("storedStaff");
        String requestURI = request.getRequestURI();
        System.out.println("Request URI in filter: " + requestURI);
        
        if(allowedURLs.contains(requestURI)){
        	//filterChain.doFilter(servletRequest, servletResponse);
        }
        
        if(studentAllowedURLs.contains(requestURI)){
            if(sessionObjStudent != null){
            	Student storedStudent = (Student) sessionObjStudent;
            	String enteredPassword = storedStudent.getPassword();
            	if (passwordEncoder.matches(enteredPassword, studentDAO.getStudentById(storedStudent.getNuid()).getPassword())) {
            		//filterChain.doFilter(servletRequest, servletResponse);
            	} 
            } else {
                System.out.println("In Student filter: Redirecting to Home page");
                response.sendRedirect("/");
                return;
            }
        }
        
        if(staffAllowedURLs.contains(requestURI)){
            if(sessionObjStaff instanceof Staff){
            	Staff storedStaff = (Staff) sessionObjStaff;
            	String enteredPassword = storedStaff.getPassword();
            	if (passwordEncoder.matches(enteredPassword, staffDAO.getStaffById(storedStaff.getNuid()).getPassword())) {
            		//filterChain.doFilter(servletRequest, servletResponse);
            	} 
            } else {
                System.out.println("In Staff filter: Redirecting to Home page");
                response.sendRedirect("/");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

