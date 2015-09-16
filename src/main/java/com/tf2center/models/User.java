package com.tf2center.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
// The keyword 'USER' is reserved by postgres
@Table(name = "WUSER", uniqueConstraints ={
		@UniqueConstraint(columnNames="Username"),
		@UniqueConstraint(columnNames="Email")
})
public class User implements Serializable {
	private static final long serialVersionUID = -3428740823850937451L;
	private Integer id;
	private String username;
	private String email;
	private String passwordHash;

	@Id
	@GeneratedValue
	@Column(name = "Id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "Username", nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "Email", nullable = false, unique = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PasswordHash", nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String toString() {
		return String.format("User [id=%s, name=%s, email=%s]", id, username, email);
	}
}
