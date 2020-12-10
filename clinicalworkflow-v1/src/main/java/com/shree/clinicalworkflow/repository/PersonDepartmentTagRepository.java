package com.shree.clinicalworkflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.User;


public interface PersonDepartmentTagRepository extends CrudRepository<PersonDepartmentTag, Long> {
	@Query(" SELECT COUNT(rt.id) "
			+ " FROM com.shree.clinicalworkflow.domain.RfidTag rt ,"
			+ " com.shree.clinicalworkflow.domain.PersonalDetails pds ,"
			+ " com.shree.clinicalworkflow.domain.PersonDepartmentTag p , "
			+ " com.shree.clinicalworkflow.domain.DepartmentModuleGroup d , "
			+ " com.shree.clinicalworkflow.domain.RfidReader r "
			+ " WHERE rt.id = :tagId "
			+ " AND rt.deactivationDate is null "
			+ " AND rt.id=pds.rfidTag.id "
			+ " AND pds.id=p.personalDetails.id "
			+ " AND p.deactivationDate is null "
			+ " AND p.department.id = d.department.id "
			+ " AND p.startTime <= :duration AND p.endTime >= :duration "
			+ " AND d.module.doorNo =:moduleId "
			+ " AND d.deactivationDate is null "
			+ " AND d.module.id = r.module.id "
			+ " AND r.readerNo= :rfidReaderId"
			+ " AND r.deactivationDate is null GROUP BY rt.id  "
			
			
			
			
			)
	Integer getAccessByTagIdModuleIdRfidReaderId(
			@Param("tagId") final Long tagId,
    		@Param("moduleId") final Integer moduleId,
    		@Param("rfidReaderId") final Integer rfidReaderId,
    		@Param("duration") final Long duration);
	
	@Query("SELECT u FROM PersonDepartmentTag u WHERE u.id = :id")
    public PersonDepartmentTag getId(@Param("id") Long id);
	
	

}
