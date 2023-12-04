package edu.northeastern.taapp.dao;

import java.util.List;

import edu.northeastern.taapp.model.Student;

public interface StudentDAO {
	
	void saveStudent(Student student);
	
	Student getStudentById(String id);
	
	Student getStudentByEmail(String email);

    List<Student> getAllStudents();

    void updateStudent(Student student);

    void deleteStudent(String id);
}
