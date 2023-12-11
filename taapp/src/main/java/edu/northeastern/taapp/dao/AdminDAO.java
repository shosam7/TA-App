package edu.northeastern.taapp.dao;

import edu.northeastern.taapp.model.Admin;

public interface AdminDAO {
	
	void saveAdmin(Admin admin);
	
	Admin getAdminById(String adminId);
}
