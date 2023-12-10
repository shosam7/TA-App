package edu.northeastern.taapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthorizationFilter implements Filter {

    private static List<String> allowedURLs = List.of("/", "/studentRegisterPage", "/studentLoginPage", "/staffRegisterPage", "/staffLoginPage", "/staffRegister", "/studentRegister");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Object sessionObjStudent = request.getSession().getAttribute("storedStudent");
        Object sessionObjStaff = request.getSession().getAttribute("storedStaff");
        String requestURI = request.getRequestURI();
        System.out.println("Request URI in filter: " + requestURI);
        if(allowedURLs.contains(requestURI)){
            if(sessionObjStudent instanceof Student){
                System.out.println("In filter: Redirecting to Student Dashboard page");
                response.sendRedirect("/studentDashboard");
                return;
            }
        }
        
        if(allowedURLs.contains(requestURI)){
            if(sessionObjStaff instanceof Staff){
                System.out.println("In filter: Redirecting to Staff Dashboard page");
                response.sendRedirect("/staffDashboard");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

