package edu.northeastern.taapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "admin")
public class Admin {
	
	@Id
    @Column(name = "admin_id")
	@Pattern(regexp = "^[0-9]{4,5}$", message = "ID must have minimum 4 to 5 digits")
    private String adminId;
	
	@Column(name = "full_name", nullable = false)
    private String fullName;

    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = ".*@northeastern\\.edu$", message = "Enter northeastern mail id")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Column(name = "password", nullable = false)
    private String password;

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
