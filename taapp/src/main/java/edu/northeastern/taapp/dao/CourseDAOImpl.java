package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.HibernateException;
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
		close();
		return allCourseList;
	}

	@Override
	public void saveCourse(Course course) {
		try {
			begin();
			getSession().persist(course);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		} finally {
			close();
		}
	}

}
