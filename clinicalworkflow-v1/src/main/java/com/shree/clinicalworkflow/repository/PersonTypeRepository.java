package com.shree.clinicalworkflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.PersonType;


public interface PersonTypeRepository extends CrudRepository<PersonType, Long> {
	@Query("SELECT r FROM PersonType r WHERE r.name = :name")
    public PersonType getPersonTypeByName(@Param("name") String name);
	
	@Query("SELECT r FROM PersonType r WHERE r.id = :id")
    public PersonType getId(@Param("id") Long id);

}
