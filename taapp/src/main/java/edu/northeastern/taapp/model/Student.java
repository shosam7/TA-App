package edu.northeastern.taapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_user")
public class Student {

    @Id
    @Column(name = "nuid", nullable = false)
    private String nuid;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "first", nullable = false)
    private String first;

    @Column(name = "last", nullable = false)
    private String last;

    @Column(name = "gpa", nullable = false)
    private Float gpa;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "entrance", nullable = false)
    private String entrance;

    @Column(name = "graduation", nullable = false)
    private String graduation;

    @Column(name = "coop")
    private String coop;

    @Column(name = "prevta")
    private String prevta;

    @Column(name = "courses")
    private String courses;

    @Lob
    @Column(name = "transcript")
    private byte[] transcript;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

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

	public byte[] getTranscript() {
		return transcript;
	}

	public void setTranscript(byte[] transcript) {
		this.transcript = transcript;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
