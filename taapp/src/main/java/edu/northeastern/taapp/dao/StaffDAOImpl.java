package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

@Repository
public class StaffDAOImpl extends DAO implements StaffDAO {

	@Override
	public void saveStaff(Staff staff) {
		try {
			begin();
			getSession().persist(staff);
			commit();
		} catch (HibernateException e) {
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public Staff getStaffById(String id) {
		Staff staff = getSession().get(Staff.class, id);
		close();
		return staff;
	}

	@Override
	public Staff getStaffByEmail(String email) {
		Query<Staff> query = getSession().createQuery("from Staff where email = :email", Staff.class);
		query.setParameter("email", email);
		Staff staff = query.uniqueResult();
		close();
		return staff;
	}

	@Override
	public void updateStaff(Staff staff) {
		try {
			begin();
			getSession().merge(staff);
			commit();
		} catch (HibernateException e) {
			rollback();
		}  finally {
			close();
		}
	}
	
	@Override
	public void deleteStaff(String id) {
		try {
			begin();
			Staff staff = getSession().get(Staff.class, id);
			if (staff != null) {
				getSession().remove(staff);
			}
			commit();
		} catch (HibernateException e) {
			rollback();
		}  finally {
			close();
		}
	}

	@Override
	public List<Staff> getAllStaffs() {
		List<Staff> allStaffs = getSession().createQuery("from Staff", Staff.class).list();
		close();
		return allStaffs;
	}

	@Override
	public List<Staff> getStaffsByKeyword(String keyword) {
		List<Staff> staffsByKeyword = getSession().createQuery("SELECT s FROM Staff s "
				+ "WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
				+ "OR LOWER(s.nuid) LIKE LOWER(CONCAT('%', :keyword, '%')) ", Staff.class)
				.setParameter("keyword", keyword)
				.list();
		close();
		return staffsByKeyword;
	}

}
