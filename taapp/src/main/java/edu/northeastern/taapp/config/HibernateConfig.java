package edu.northeastern.taapp.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Course;
import edu.northeastern.taapp.model.Job;
import edu.northeastern.taapp.model.Staff;
import edu.northeastern.taapp.model.Student;

public class HibernateConfig {

    public static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Staff.class);
            configuration.addAnnotatedClass(Job.class);
            configuration.addAnnotatedClass(Course.class);
            configuration.addAnnotatedClass(Application.class);
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/tadb");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "12345678");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error building Hibernate SessionFactory: " + e.getMessage(), e);
        }
    }
}