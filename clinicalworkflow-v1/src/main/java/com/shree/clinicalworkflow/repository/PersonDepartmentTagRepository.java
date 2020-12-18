package com.shree.clinicalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.User;
import com.shree.clinicalworkflow.dto.LogData;


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
	
	@Query("SELECT  new com.shree.clinicalworkflow.dto.LogData(T7.log,COUNT(T7.log)) "+ 
			" FROM com.shree.clinicalworkflow.domain.PersonType T1," + 
			" com.shree.clinicalworkflow.domain.PersonalDetails T2," + 
			" com.shree.clinicalworkflow.domain.PersonDepartmentTag T3," + 
			" com.shree.clinicalworkflow.domain.Module T5," + 
			" com.shree.clinicalworkflow.domain.RfidReader T8," + 
			" com.shree.clinicalworkflow.domain.PersonDepartmentTagLog T7" + 
			" WHERE T1.deactivationDate IS NULL" + 
			" AND T1.dept = :module " + 
			" AND T1.id =T2.personType.id " + 
			" AND T2.id =T3.personalDetails.id" +
			" AND T2.log is not null " +
			" AND T2.log = T7.log " +
			" AND T3.deactivationDate IS NULL" + 
			" AND T3.module.id =T5.id " + 
			" AND T5.deactivationDate IS NULL" + 
			" AND T5.id =T8.module.id " +
			" AND T8.module.doorNo=1 " +
			" AND T8.deactivationDate IS NULL" + 
			" AND T2.id = T7.tagId " + 
			" AND T7.permission IN ('GRANTED')" + 
			" AND T5.id=T7.moduleId" + 
			" AND T8.id =T7.rfidReaderId" + 
			" AND T7.logTime=(SELECT MAX(S1.logTime)" + 
			"                                    FROM com.shree.clinicalworkflow.domain.PersonDepartmentTagLog S1 " + 
			"                                    WHERE S1.log IN('IN','OUT') " + 
			"                                    AND Get_Date(S1.logTime) =Get_Date(T7.logTime) " + 
			"                                    AND S1.moduleId =T7.moduleId " + 
			"                                    AND S1.permission =T7.permission " + 
			"                                    AND S1.tagId=T7.tagId" + 
			"                                    GROUP BY S1.tagId )" + 
			" GROUP BY T7.log ")
	List<LogData> getLogCountByModuleAndLog(
			@Param("module") final String moduleId);
	
	@Query(" SELECT new com.shree.clinicalworkflow.dto.LogData(T7.log,COUNT(T7.tagId)) "+
			" FROM com.shree.clinicalworkflow.domain.PersonType T1," + 
			" com.shree.clinicalworkflow.domain.PersonalDetails T2," + 
			" com.shree.clinicalworkflow.domain.PersonDepartmentTag T3," + 
			" com.shree.clinicalworkflow.domain.DepartmentModuleGroup T4," +
			" com.shree.clinicalworkflow.domain.Module T5," + 
			" com.shree.clinicalworkflow.domain.RfidReader T8," + 
			" com.shree.clinicalworkflow.domain.PersonDepartmentTagLog T7" + 
			" WHERE T1.deactivationDate IS NULL" + 
			" AND T1.dept = :module " + 
			" AND T1.id =T2.personType.id " + 
			" AND T2.id =T3.personalDetails.id" +
			" AND T2.log is not null " +
			" AND T2.log = T7.log " +
			" AND T3.deactivationDate IS NULL" +
			" AND T3.department.id=T4.department.id " +  
			" AND T4.deactivationDate  IS NULL "+
			
    		" AND T4.module.id =T5.id "+
			" AND T5.deactivationDate IS NULL" + 
			" AND T5.id =T8.module.id " +
			" AND T8.module.doorNo=1 " +
			" AND T8.deactivationDate IS NULL" + 
			" AND T2.id = T7.tagId " + 
			" AND T7.permission IN ('GRANTED')" + 
			" AND T5.id=T7.moduleId" + 
			" AND T8.id =T7.rfidReaderId" + 
			" AND T7.logTime=(SELECT MAX(S1.logTime)" + 
			"                                    FROM com.shree.clinicalworkflow.domain.PersonDepartmentTagLog S1 " + 
			"                                    WHERE S1.log IN('IN','OUT') " + 
			"                                    AND Get_Date(S1.logTime) =Get_Date(T7.logTime) " + 
			"                                    AND S1.moduleId =T7.moduleId " + 
			"                                    AND S1.permission =T7.permission " + 
			"                                    AND S1.tagId=T7.tagId" + 
			"                                    GROUP BY S1.tagId )" + 
			" GROUP BY T7.log ,T7.tagId ")
	List<LogData> getLogCountByDeptAndLog(
			@Param("module") final String moduleId);

}
