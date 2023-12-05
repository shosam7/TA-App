package edu.northeastern.taapp.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Course;

@Repository
public class CourseDAOImpl extends DAO implements CourseDAO {

	@Override
	public Course getCourseById(String id) {
		Course course = getSession().get(Course.class, id);
		return course;
	}

	@Override
	public List<Course> getAllCourses() {
		List<Course> allCourseList = getSession().createQuery("from Course", Course.class).list();
		return allCourseList;
	}

}
