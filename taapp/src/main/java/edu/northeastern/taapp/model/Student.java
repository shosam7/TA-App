package edu.northeastern.taapp.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "student_user")
public class Student {

    @Id
    @Column(name = "nuid", nullable = false)
    @Pattern(regexp = "^[0-9]{8,12}$", message = "NUID must have minimum 8 to 12 digits")
    private String nuid;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = ".*@northeastern\\.edu$", message = "Enter northeastern mail id")
    @Column(name = "email", nullable = false)
    private String email;
    
    @NotBlank(message = "First name must not be blank")
    @Column(name = "first", nullable = false)
    private String first;
    
    @NotBlank(message = "Last name must not be blank")
    @Column(name = "last", nullable = false)
    private String last;
    
    @Column(name = "gpa", nullable = false)
    private Float gpa;
    
    @NotBlank(message = "Major must not be blank")
    @Column(name = "major", nullable = false)
    private String major;
    
    @NotBlank(message = "Entrance date must not be blank")
    @Column(name = "entrance", nullable = false)
    private String entrance;
    
    @NotBlank(message = "Graduation date must not be blank")
    @Column(name = "graduation", nullable = false)
    private String graduation;

    @Column(name = "coop")
    private String coop;

    @Column(name = "prevta")
    private String prevta;
    
    @NotBlank(message = "Courses must not be blank")
    @Column(name = "courses")
    private String courses;
    
    @Transient
    private MultipartFile transcript;

    @Transient
    private MultipartFile photo;
    
    @Column(name = "transcript_path")
    private String transcriptPath;

    @Column(name = "photo_path")
    private String photoPath;
    
    @NotBlank(message = "Password must not be blank")
    @Column(name = "password", nullable = false)
    private String password;

	public Student() {
		super();
	}

	public String getNuid() {
		return nuid;
	}

	public void setNuid(String nuid) {
		this.nuid = nuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Float getGpa() {
		return gpa;
	}

	public void setGpa(Float gpa) {
		this.gpa = gpa;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getEntrance() {
		return entrance;
	}

	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}

	public String getCoop() {
		return coop;
	}

	public void setCoop(String coop) {
		this.coop = coop;
	}

	public String getPrevta() {
		return prevta;
	}

	public void setPrevta(String prevta) {
		this.prevta = prevta;
	}

	public String getCourses() {
		return courses;
	}

	public void setCourses(String courses) {
		this.courses = courses;
	}

	public MultipartFile getTranscript() {
		return transcript;
	}

	public void setTranscript(MultipartFile transcript) {
		this.transcript = transcript;
	}

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	public String getTranscriptPath() {
		return transcriptPath;
	}

	public void setTranscriptPath(String transcriptPath) {
		this.transcriptPath = transcriptPath;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
