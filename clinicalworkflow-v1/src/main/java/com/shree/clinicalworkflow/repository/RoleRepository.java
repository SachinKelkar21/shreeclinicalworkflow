package com.shree.clinicalworkflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.name = :name")
    public Role getRoleByName(@Param("name") String name);
	
	@Query("SELECT r FROM Role r WHERE r.id = :id")
    public Role getId(@Param("id") Long id);
}
