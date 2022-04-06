package com.shree.clinicalworkflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.RfidTagStatus;


public interface RfidTagRepository extends CrudRepository<RfidTag, Long> {
	@Query("SELECT r FROM RfidTag r WHERE r.rfidTagHexNo = :rfidTagHexNo AND r.deactivationDate IS NULL ")
    public RfidTag getRfidTagByRfidTagHexNo(@Param("rfidTagHexNo") String rfidTagHexNo);
	
	@Query("SELECT r FROM RfidTag r WHERE r.id = :id AND r.deactivationDate IS NULL ")
    public RfidTag getId(@Param("id") Long id);

	@Query("SELECT r FROM RfidTag r WHERE r.status = :status AND r.deactivationDate IS NULL ")
    public List<RfidTag> getAllRfidTagByStatus(RfidTagStatus status);
	/*
	@Query("SELECT r.personalDetails FROM RfidTag r WHERE r.status = :status AND r.deactivationDate IS NULL ")
    public List<PersonalDetails> getAllPersonalDetailsByStatus(RfidTagStatus status);
	
	@Query("SELECT r FROM RfidTag r WHERE r.personalDetails.id = :id and r.status = :status AND r.deactivationDate IS NULL ")
    public RfidTag getPersonalDetailsByPersonalDetailsId(@Param("id") Long id,@Param("status") RfidTagStatus status);
	
*/
}
