package edu.northeastern.taapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "staff")
public class Staff {
	
	@Id
    @Column(name = "staff_nuid")
	@Pattern(regexp = "^[0-9]{8,12}$", message = "NUID must have minimum 8 to 12 digits")
    private String nuid;

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
    
    public Staff() {
    	
    }

	public String getNuid() {
		return nuid;
	}

	public void setNuid(String nuid) {
		this.nuid = nuid;
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
