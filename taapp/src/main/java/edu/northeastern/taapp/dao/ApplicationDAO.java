package edu.northeastern.taapp.dao;

import java.util.List; 

import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Application.Status;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationDAO {
	
	void saveApplication(Application application);
	
	Application getApplicationByStudentAndJob(Student student, Job job);
	
	Page<Application> getApplicationsByStaff(Staff staff, Pageable pageable);
	
	Page<Application> getApplicationsByStaffAndJob(Staff staff, Job job, Pageable pageable);
	
	Page<Application> getApplicationsByStaffAndStatus(Staff staff, String status, Pageable pageable);
	
	Page<Application> getFilteredApplications(Staff staff, String status, Job job, Pageable pageable);
	
	Application getApplicationById(Long applicationId);
	
	void updateApplication(Application application);
	
	List<Application> getApplicationsByStudent(Student student);
	
	void deleteApplication(Long applicationId);
	
	List<Application> getApplicationsByStudentAndStatus(Student student, Status status);
	
	List<Application> getApplicationsByJob(Job job);
	
	List<Application> getApplicationsByStaff(Staff staff);
}
