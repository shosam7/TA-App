package edu.northeastern.taapp.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.model.Admin;

@Repository
public class AdminDAOImpl extends DAO implements AdminDAO {

	@Override
	public void saveAdmin(Admin admin) {
		try {
			begin();
			getSession().persist(admin);
			commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			rollback();
		} finally {
			close();
		}
	}

	@Override
	public Admin getAdminById(String adminId) {
		Admin admin = getSession().get(Admin.class, adminId);
		close();
		return admin;
	}

}
