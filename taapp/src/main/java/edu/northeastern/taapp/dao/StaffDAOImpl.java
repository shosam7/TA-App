package edu.northeastern.taapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import edu.northeastern.taapp.config.HibernateConfig;
import edu.northeastern.taapp.model.Staff;

@Repository
public class StaffDAOImpl implements StaffDAO {

	SessionFactory sessionFactory = HibernateConfig.buildSessionFactory();

	@Override
	public void saveStaff(Staff staff) {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.persist(staff);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Staff getStaffById(String id) {
		try (Session session = sessionFactory.openSession()) {
			Staff staff = session.get(Staff.class, id);
			return staff;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Staff getStaffByEmail(String email) {
		try (Session session = sessionFactory.openSession()) {
            Query<Staff> query = session.createQuery("from Staff where email = :email", Staff.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public List<Staff> getAllStaff() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStaff(Staff staff) {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.merge(staff);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteStaff(String id) {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.getTransaction();
			transaction.begin();
			Staff staff = session.get(Staff.class, id);
			if (staff != null) {
				session.remove(staff);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
