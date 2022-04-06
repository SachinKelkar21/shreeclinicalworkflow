package com.shree.clinicalworkflow.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.PersonDepartmentTagLog;
import com.shree.clinicalworkflow.dto.PersonLog;

public interface PersonDepartmentTagLogRepository extends CrudRepository<PersonDepartmentTagLog, Long> {
    @Modifying
    @Transactional
	@Query(" DELETE FROM com.shree.clinicalworkflow.domain.PersonDepartmentTagLog T7 WHERE T7.logTime < :date ") 
	int removeOneWeekDataOnEverySunday(@Param("date") Long date );
    
    @Query(value=" SELECT new com.shree.clinicalworkflow.dto.PersonLog(T1.logTime,T1.permission,T2.doorNo,T1.log) FROM com.shree.clinicalworkflow.domain.PersonDepartmentTagLog T1 , com.shree.clinicalworkflow.domain.Module T2 WHERE Get_Date(T1.logTime) >= Get_Date(:log_Time) AND Get_Date(T1.logTime) <= Get_Date(:log_Time2) AND T1.tagId = :tag_Id  AND T1.moduleId = T2.id  AND T2.deactivationDate IS NULL ORDER BY T1.id DESC ",nativeQuery = false)
	List<PersonLog> getPersonLogByLogTime(@Param("log_Time") final Long logDateTime,@Param("log_Time2") final Long logDateTime2,@Param("tag_Id") final Long tagId);
	
    	
}
