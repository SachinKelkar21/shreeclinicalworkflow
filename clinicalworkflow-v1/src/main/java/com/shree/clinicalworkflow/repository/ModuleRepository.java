package com.shree.clinicalworkflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import com.shree.clinicalworkflow.domain.Module;

public interface ModuleRepository extends CrudRepository<Module, Long> {

	@Query("SELECT m FROM Module m WHERE m.doorNo = :doorNo")
    public Module getModuleByDoorNo(@Param("doorNo") Integer doorNo);
}
