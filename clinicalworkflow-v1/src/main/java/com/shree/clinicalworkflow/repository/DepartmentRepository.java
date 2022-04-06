package com.shree.clinicalworkflow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.shree.clinicalworkflow.domain.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
	@Query("SELECT d FROM Department d WHERE d.name = :name AND d.deactivationDate is null ")
    public Department getDepartmentByName(@Param("name") String name);
	
	@Query("SELECT d FROM Department d WHERE d.name != 'ALL' AND d.deactivationDate is null ")
    public List<Department> getAllDepartmentExclusingAll();
}
