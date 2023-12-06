package edu.northeastern.taapp.dao;

import java.util.List;

import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

public interface ApplicationDAO {
	
	void saveApplication(Application application);
	
	Application getApplicationByStudentAndJob(Student student, Job job);
	
	List<Application> getApplicationsByStaff(Staff staff);
	
	Application getApplicationById(Long applicationId);
	
	void updateApplication(Application application);
}
