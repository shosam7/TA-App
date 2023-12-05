package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Staff;

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
		}
	}

	@Override
	public Staff getStaffById(String id) {
		Staff staff = getSession().get(Staff.class, id);
		return staff;
	}

	@Override
	public Staff getStaffByEmail(String email) {
		Query<Staff> query = getSession().createQuery("from Staff where email = :email", Staff.class);
		query.setParameter("email", email);
		return query.uniqueResult();
	}

	@Override
	public List<Staff> getAllStaff() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStaff(Staff staff) {
		try {
			begin();
			getSession().merge(staff);
			commit();
		} catch (HibernateException e) {
			rollback();
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
		}
	}

}
