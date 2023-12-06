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
		}
	}

	@Override
	public Student getStudentById(String id) {
		Student student = getSession().get(Student.class, id);
		return student;
	}

	@Override
	public List<Student> getAllStudents() {
		List<Student> allStudentsList = getSession().createQuery("from Student", Student.class).list();
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
		}
	}

	@Override
	public Student getStudentByEmail(String email) {
			Query<Student> query = getSession().createQuery("from Student where email = :email", Student.class);
			query.setParameter("email", email);
			return query.uniqueResult();
	}
}
