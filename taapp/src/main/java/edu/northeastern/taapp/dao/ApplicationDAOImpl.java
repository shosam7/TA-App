package edu.northeastern.taapp.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Application.Status;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;
import jakarta.persistence.TypedQuery;

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

//	@Override
//	public List<Application> getApplicationsByStaff(Staff staff) {
//		String hql = "FROM Application a WHERE a.staff = :staff";
//		List<Application> applications = getSession().createQuery(hql, Application.class).setParameter("staff", staff).list();
//		close();
//		return applications;
//	}
	
	@Override
    public Page<Application> getApplicationsByStaff(Staff staff, Pageable pageable) {
        String hql = "FROM Application a WHERE a.staff = :staff";
        
        TypedQuery<Application> query = getSession().createQuery(hql, Application.class)
            .setParameter("staff", staff)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize());

        List<Application> applications = query.getResultList();
        long totalCount = getTotalCountByStaff(staff);
        close();
        return new PageImpl<>(applications, pageable, totalCount);
    }

    private long getTotalCountByStaff(Staff staff) {
        String countQuery = "SELECT COUNT(a) FROM Application a WHERE a.staff = :staff";
        long count = getSession().createQuery(countQuery, Long.class).setParameter("staff", staff).getSingleResult();
        close();
        return count;
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
	
	@Override
	public Page<Application> getFilteredApplications(
	        Staff staff, String status, Job job, Pageable pageable) {
		String hql = "FROM Application a WHERE a.staff = :staff AND a.status = :status AND a.job = :job";
	    TypedQuery<Application> query = getSession().createQuery(hql, Application.class)
	            .setParameter("staff", staff)
	            .setParameter("status", Status.valueOf(status))
	            .setParameter("job", job)
	            .setFirstResult((int) pageable.getOffset())
	            .setMaxResults(pageable.getPageSize());

	    List<Application> applications = query.getResultList();
	    long totalCount = getTotalCountByFilters(staff, status, job); 
	    close();
	    return new PageImpl<>(applications, pageable, totalCount);
	}

	private long getTotalCountByFilters(Staff staff, String status, Job job) {
		String countQuery = "SELECT COUNT(a) FROM Application a WHERE a.staff = :staff AND a.status = :status AND a.job = :job";
        long count = getSession().createQuery(countQuery, Long.class)
        		.setParameter("staff", staff)
        		.setParameter("status", Status.valueOf(status))
	            .setParameter("job", job)
        		.getSingleResult();
        close();
	    return count;
	}

	@Override
	public Page<Application> getApplicationsByStaffAndStatus(Staff staff, String status, Pageable pageable) {
		String hql = "FROM Application a WHERE a.staff = :staff AND a.status = :status";
	    TypedQuery<Application> query = getSession().createQuery(hql, Application.class)
	            .setParameter("staff", staff)
	            .setParameter("status", Status.valueOf(status))
	            .setFirstResult((int) pageable.getOffset())
	            .setMaxResults(pageable.getPageSize());

	    List<Application> applications = query.getResultList();
	    long totalCount = getApplicationsCountByStaffAndStatus(staff, status); 
	    close();
	    return new PageImpl<>(applications, pageable, totalCount);
	}

	
	private long getApplicationsCountByStaffAndStatus(Staff staff, String status) {
		String countQuery = "SELECT COUNT(a) FROM Application a WHERE a.staff = :staff AND a.status = :status";
        long count = getSession().createQuery(countQuery, Long.class)
        		.setParameter("staff", staff)
        		.setParameter("status", Status.valueOf(status))
        		.getSingleResult();
        close();
	    return count;
	}

	@Override
	public Page<Application> getApplicationsByStaffAndJob(Staff staff, Job job, Pageable pageable) {
		String hql = "FROM Application a WHERE a.staff = :staff AND a.job = :job";
	    TypedQuery<Application> query = getSession().createQuery(hql, Application.class)
	            .setParameter("staff", staff)
	            .setParameter("job", job)
	            .setFirstResult((int) pageable.getOffset())
	            .setMaxResults(pageable.getPageSize());

	    List<Application> applications = query.getResultList();
	    long totalCount = getApplicationsCountByStaffAndJob(staff, job); 
	    close();
	    return new PageImpl<>(applications, pageable, totalCount);
	}

	private long getApplicationsCountByStaffAndJob(Staff staff, Job job) {
		String countQuery = "SELECT COUNT(a) FROM Application a WHERE a.staff = :staff AND a.job = :job";
        long count = getSession().createQuery(countQuery, Long.class)
        		.setParameter("staff", staff)
        		.setParameter("job", job)
        		.getSingleResult();
        close();
	    return count;
	}
}
