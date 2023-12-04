package edu.northeastern.taapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
	    name = "job",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"staff_nuid", "course_name"})
	    }
	)
public class Job {

    @Id
    @Column(name = "job_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "staff_nuid")
    private Staff staff;
    
    @Column(name = "course_name", nullable = false)
    private String courseName;
    
    @NotBlank(message = "Select atleast 1")
    @Column(name = "num_openings", nullable = false)
    private int numOpenings;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getNumOpenings() {
		return numOpenings;
	}

	public void setNumOpenings(int numOpenings) {
		this.numOpenings = numOpenings;
	}
    
    
}

