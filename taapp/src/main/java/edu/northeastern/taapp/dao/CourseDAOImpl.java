package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.config.HibernateConfig;
import edu.northeastern.taapp.model.Course;

@Repository
public class CourseDAOImpl implements CourseDAO{
	
	SessionFactory sessionFactory = HibernateConfig.buildSessionFactory();
	
	@Override
	public Course getCourseById(String id) {
		try(Session session = sessionFactory.openSession()){
            Course course = session.get(Course.class, id);
            return course;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public List<Course> getAllCourses() {
		try(Session session = sessionFactory.openSession()){
            List<Course> allCourseList = session.createQuery("from Course", Course.class).list();
            return allCourseList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

}
