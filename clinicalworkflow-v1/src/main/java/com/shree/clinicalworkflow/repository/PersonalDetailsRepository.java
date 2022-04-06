package com.shree.clinicalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidTagStatus;

public interface PersonalDetailsRepository extends CrudRepository<PersonalDetails, Long> {
	@Query("SELECT p FROM PersonalDetails p WHERE p.code = :code")
    public PersonalDetails getPersonalDetailsByCode(@Param("code") String code);
	
	@Query("SELECT u FROM PersonalDetails u WHERE u.id = :id")
    public PersonalDetails getId(@Param("id") Long id);
	
	@Query("SELECT u FROM PersonalDetails u WHERE u.rfidTag.id = :id AND u.rfidTag.status = :status AND u.rfidTag.deactivationDate IS NUll ")
    public PersonalDetails getPersonalDetailsByRfidTagIdAndStatus(@Param("id") Long id,@Param("status") RfidTagStatus status);

	@Query("SELECT u FROM PersonalDetails u WHERE u.rfidTag.rfidTagHexNo = :hexNo AND u.rfidTag.status = :status AND u.rfidTag.deactivationDate IS NUll ")
    public PersonalDetails getPersonalDetailsByRfidTagHexNoAndStatus(@Param("hexNo") String hexNo,@Param("status") RfidTagStatus status);

	@Query("SELECT u FROM PersonalDetails u WHERE u.rfidTag.rfidTagHexNo = :hexNo AND u.rfidTag.deactivationDate IS NUll")
    public PersonalDetails getPersonalDetailsByRfidTagHexNo(@Param("hexNo") String hexNo);
	
	@Query("SELECT p FROM PersonalDetails p WHERE  p.rfidTag.rfidTagHexNo IS NOT NULL AND  p.rfidTag.status = :status AND p.personType.deactivationDate IS NUll  AND p.personType.dept = :dept ORDER BY p.rfidTag.lastUpdated DESC ")
    public List<PersonalDetails> getAllPersonalDetailsByRfidTagStatusDept(@Param("status") RfidTagStatus status,@Param("dept") String dept);
	
	@Query("SELECT p FROM PersonalDetails p WHERE  p.rfidTag.rfidTagHexNo IS NOT NULL AND  p.rfidTag.status = :status AND p.personType.deactivationDate IS NUll  AND p.personType.dept = :dept AND p.code = Upper(:keywords) ORDER BY p.rfidTag.lastUpdated DESC ")
    public List<PersonalDetails> getAllPersonalDetailsByRfidTagStatusDeptKeyword(@Param("status") RfidTagStatus status,@Param("dept") String dept,@Param("keywords") String keyword);
	
	@Modifying
	@Transactional
	@Query(value = "BACKUP TO ?1", nativeQuery = true)
	int backupDB(String path);
	
	@Query("SELECT p FROM PersonalDetails p WHERE  p.rfidTag.rfidTagHexNo IS NOT NULL AND  p.rfidTag.status = :status AND p.personType.deactivationDate IS NUll  AND p.personType.dept = :dept AND p.code = Upper(:keywords) ORDER BY p.rfidTag.lastUpdated DESC ")
    public List<PersonalDetails> getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByCode(@Param("status") RfidTagStatus status,@Param("dept") String dept,@Param("keywords") String keyword);
	
	@Query("SELECT p FROM PersonalDetails p WHERE  p.rfidTag.rfidTagHexNo IS NOT NULL AND  p.rfidTag.status = :status AND p.personType.deactivationDate IS NUll  AND p.personType.dept = :dept AND p.firstName = Upper(:keywords) ORDER BY p.rfidTag.lastUpdated DESC ")
    public List<PersonalDetails> getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByFirstName(@Param("status") RfidTagStatus status,@Param("dept") String dept,@Param("keywords") String keyword);
	
	@Query("SELECT p FROM PersonalDetails p WHERE  p.rfidTag.rfidTagHexNo IS NOT NULL AND  p.rfidTag.status = :status AND p.personType.deactivationDate IS NUll  AND p.personType.dept = :dept AND p.lastName = Upper(:keywords) ORDER BY p.rfidTag.lastUpdated DESC ")
    public List<PersonalDetails> getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByLastName(@Param("status") RfidTagStatus status,@Param("dept") String dept,@Param("keywords") String keyword);
}
