package com.shree.clinicalworkflow.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public  class User {
	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	private Long id;
	
	private String name;
	@Column(length = 60)
    private String password;
    private boolean enabled;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="USER_ROLE",
    		joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    		)
    private  Set<Role> roles;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }
 
    public void removeUser(Role role) {
        this.getRoles().remove(role);
        role.getUsers().remove(this);
    }
 
    public void remove() {
        for(Role role : new HashSet<>(roles)) {
            removeUser(role);
        }
    }


}
