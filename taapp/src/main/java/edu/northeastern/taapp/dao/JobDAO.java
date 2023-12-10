package edu.northeastern.taapp.dao;

import java.util.List;

import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;

public interface JobDAO {

	void saveJob(Job job);
	
	Job getJobById(String id);
	
	List<Job> getAllJobs();
	
	List<Staff> getUniqueStaffList();
	
	List<Job> getJobsByStaff(Staff staff);
	
	Job getJobByStaffAndCourse(Staff staff, Course course);
	
	void updateJob(Job job);
	
	void deleteJob(Long jobId);
}
