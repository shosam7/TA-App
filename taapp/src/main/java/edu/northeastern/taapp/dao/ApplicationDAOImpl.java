package edu.northeastern.taapp.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Application.Status;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

@Repository
public class ApplicationDAOImpl extends DAO implements ApplicationDAO {

	@Override
	public void saveApplication(Application application) {
		try {
			begin();
			getSession().persist(application);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public Application getApplicationByStudentAndJob(Student student, Job job) {
		String hql = "FROM Application a WHERE a.student = :student AND a.job = :job";
		Application application = getSession().createQuery(hql, Application.class).setParameter("student", student)
				.setParameter("job", job).uniqueResult();
		close();
		return application;
	}

	@Override
	public List<Application> getApplicationsByStaff(Staff staff) {
		String hql = "FROM Application a WHERE a.staff = :staff";
		List<Application> applications = getSession().createQuery(hql, Application.class).setParameter("staff", staff).list();
		close();
		return applications;
	}

	@Override
	public Application getApplicationById(Long applicationId) {
		Application application = getSession().get(Application.class, applicationId);
		close();
		return application;
	}

	@Override
	public void updateApplication(Application application) {
		try {
			begin();
			getSession().merge(application);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public List<Application> getApplicationsByStudent(Student student) {
		String hql = "FROM Application a WHERE a.student = :student";
		List<Application> applications = getSession().createQuery(hql, Application.class).setParameter("student", student).list();
		close();
		return applications;
	}

	@Override
	public void deleteApplication(Long applicationId) {
		try {
			begin();
			Application application = getSession().get(Application.class, applicationId);
			if (application != null) {
				getSession().remove(application);
			}
			commit();
		} catch (HibernateException e) {
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public List<Application> getApplicationsByStudentAndStatus(Student student, Status status) {
		try {
			String hql = "FROM Application a WHERE a.student = :student AND a.status = :status";
			return getSession().createQuery(hql, Application.class).setParameter("student", student)
					.setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			close();
		}
	}

	@Override
	public List<Application> getApplicationsByJob(Job job) {
		try {
			String hql = "FROM Application a WHERE a.job = :job";
			return getSession().createQuery(hql, Application.class).setParameter("job", job).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			close();
		}
	}
}
