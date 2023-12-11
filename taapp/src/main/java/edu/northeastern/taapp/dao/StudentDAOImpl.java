package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Student;

@Repository
public class StudentDAOImpl extends DAO implements StudentDAO {

	@Override
	public void saveStudent(Student student) {
		try {
			begin();
			getSession().persist(student);
			commit();
		} catch (HibernateException e) {
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public Student getStudentById(String id) {
		Student student = getSession().get(Student.class, id);
		close();
		return student;
	}

	@Override
	public List<Student> getAllStudents() {
		List<Student> allStudentsList = getSession().createQuery("from Student", Student.class).list();
		close();
		return allStudentsList;
	}

	@Override
	public void updateStudent(Student student) {
		try {
			begin();
			getSession().merge(student);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public void deleteStudent(String id) {
		try {
			begin();
			Student student = getSession().get(Student.class, id);
			if (student != null) {
				getSession().remove(student);
			}
			commit();
		} catch (HibernateException e) {
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public Student getStudentByEmail(String email) {
		Query<Student> query = getSession().createQuery("from Student where email = :email", Student.class);
		query.setParameter("email", email);
		Student student = query.uniqueResult();
		close();
		return student;
	}
}
