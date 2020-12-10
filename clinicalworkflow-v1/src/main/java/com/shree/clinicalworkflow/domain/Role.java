package com.shree.clinicalworkflow.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role {
	@Id
	@GeneratedValue
	@Column(name="ROLE_ID")
	private Long id;
	
	private String name;
	
	@ManyToMany(mappedBy="roles")
	private  Set<User> users =new HashSet<User>();
	
	public Role() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public Set<User> getUsers() {
		return users;
	}

}
