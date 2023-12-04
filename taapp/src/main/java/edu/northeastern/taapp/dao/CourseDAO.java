package edu.northeastern.taapp.dao;

import java.util.List;

import edu.northeastern.taapp.model.Course;

public interface CourseDAO {
	
	Course getCourseById(String id);

    List<Course> getAllCourses();
}
