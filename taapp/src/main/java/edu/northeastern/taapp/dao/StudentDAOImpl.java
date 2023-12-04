package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.config.HibernateConfig;
import edu.northeastern.taapp.model.Student;

@Repository
public class StudentDAOImpl implements StudentDAO {
	
	SessionFactory sessionFactory = HibernateConfig.buildSessionFactory();
	
	@Override
	public void saveStudent(Student student) {
		try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(student);
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }		
	}

	@Override
	public Student getStudentById(String id) {
		try(Session session = sessionFactory.openSession()){
            Student student = session.get(Student.class, id);
            return student;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public List<Student> getAllStudents() {
		try(Session session = sessionFactory.openSession()){
            List<Student> allStudentsList = session.createQuery("from student_user").list();
            return allStudentsList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public void updateStudent(Student student) {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void deleteStudent(String id) {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public Student getStudentByEmail(String email) {
		try (Session session = sessionFactory.openSession()) {
            Query<Student> query = session.createQuery("from Student where email = :email", Student.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
