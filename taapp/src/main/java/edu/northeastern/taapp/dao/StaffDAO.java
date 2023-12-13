package edu.northeastern.taapp.dao;

import java.util.List; 

import edu.northeastern.taapp.model.Staff;

public interface StaffDAO {
	
	void saveStaff(Staff staff);
	
	Staff getStaffById(String id);
	
	Staff getStaffByEmail(String email);

    void updateStaff(Staff staff);

    void deleteStaff(String id);
    
    List<Staff> getAllStaffs();
    
    List<Staff> getStaffsByKeyword(String keyword);
}