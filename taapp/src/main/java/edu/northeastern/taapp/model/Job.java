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

@Entity
@Table(
	    name = "job",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"staff_nuid", "course_id"})
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
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(name = "num_openings", nullable = false)
    private int numOpenings;
    
    @Column(length = 3000)
    private String requirements;
    
    public Job() {
    	this.numOpenings= 1;
    }

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getNumOpenings() {
		return numOpenings;
	}

	public void setNumOpenings(int numOpenings) {
		this.numOpenings = numOpenings;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	
}

