package edu.northeastern.taapp.dao;

import java.util.List; 

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;

@Repository
public class JobDAOImpl extends DAO implements JobDAO {

	@Override
	public void saveJob(Job job) {
		try {
			begin();
			getSession().persist(job);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public Job getJobById(String id) {
		Job job = getSession().get(Job.class, id);
		close();
		return job;
	}

	@Override
	public List<Job> getAllJobs() {
		List<Job> allJobsList = getSession().createQuery("from Job", Job.class).list();
		close();
		return allJobsList;
	}

	@Override
	public List<Staff> getUniqueStaffList() {
		List<Staff> uniqueStaffList = getSession().createQuery("SELECT DISTINCT j.staff FROM Job j", Staff.class).list();
		close();
		return uniqueStaffList;
	}

	@Override
	public List<Job> getJobsByStaff(Staff staff) {
		List<Job> jobsByStaff = getSession().createQuery("FROM Job j WHERE j.staff = :staff", Job.class).setParameter("staff", staff).list();
		close();
		return jobsByStaff;
	}

	@Override
	public Job getJobByStaffAndCourse(Staff staff, Course course) {
		String hql = "FROM Job j WHERE j.staff = :staff AND j.course = :course";
        Job job = getSession().createQuery(hql, Job.class)
                .setParameter("staff", staff)
                .setParameter("course", course)
                .uniqueResult();
        close();
        return job;
	}

	@Override
	public void updateJob(Job job) {
		try {
			begin();
			getSession().merge(job);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public void deleteJob(Long jobId) {
		try {
			begin();
			Job job = getSession().get(Job.class, jobId);
			if (job != null) {
				getSession().remove(job);
			}
			commit();
		} catch (HibernateException e) {
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public List<Job> getJobsByCourse(Course course) {
		List<Job> jobsByCourse = getSession().createQuery("FROM Job j WHERE j.course = :course", Job.class).setParameter("course", course).list();
		close();
		return jobsByCourse;
	}
}
