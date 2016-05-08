package org.nightswimming.thesis.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String name;
//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
//	private Set<User> users = new HashSet<User>();

	protected Role(){}
	
	public Role(String name){
		this.name = name;
	}
	
	public String getName() 	     { return name; }
	public void setName(String name) { this.name = name;}
//	public Set<User> getUsers()   { return users; }
//	public void setUsers(Set<User> users) { this.users = users; }
}
