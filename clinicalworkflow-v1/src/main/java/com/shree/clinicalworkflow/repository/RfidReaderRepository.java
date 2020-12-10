package com.shree.clinicalworkflow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shree.clinicalworkflow.domain.RfidReader;


public interface RfidReaderRepository extends CrudRepository<RfidReader, Long> {

	@Query("SELECT r FROM RfidReader r WHERE r.id = :id AND r.deactivationDate IS NULL ")
    public RfidReader getId(@Param("id") Long id);
	

	@Query("SELECT r FROM RfidReader r WHERE r.module.doorNo = :doorNo AND r.module.deactivationDate IS NULL AND r.readerNo= :readerNo AND r.deactivationDate IS NULL ")
    public RfidReader getRfidReaderByDoorNoReaderNo(@Param("doorNo") Integer doorNo,@Param("readerNo") Integer readerNo);
}
