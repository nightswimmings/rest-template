package org.nightswimming.thesis.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String username;
	private String password;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Role> roles;

	protected User(){}
	
	public User(String username, String password, List<Role> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public Integer getId()                      { return id; }
	public void    setId(Integer id)            { this.id = id; }
	public String  getUserName()                { return username; }
	public void    setUserName(String name) 	{ this.username = name; }
	public String  getPassword()                { return password; }
	public void    setPassword(String pass)     { this.password = pass; }
	public List<Role> getRoles()                { return roles; }
	public void    setRoles(List<Role> roles)   { this.roles = roles; }
}
