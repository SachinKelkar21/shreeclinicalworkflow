package com.shree.clinicalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.name = :name")
    public User getUserByName(@Param("name") String name);
	
	@Query("SELECT u FROM User u WHERE u.id = :id")
    public User getId(@Param("id") Long id);
	
}
