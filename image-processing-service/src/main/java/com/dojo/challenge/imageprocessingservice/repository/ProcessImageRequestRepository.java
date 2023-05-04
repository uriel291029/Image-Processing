package com.dojo.challenge.imageprocessingservice.repository;

import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessImageRequestRepository extends MongoRepository<ProcessImageRequest, String> {
	
	List<ProcessImageRequest> findByBeginDateBetween(LocalDate startDate, LocalDate endDate);
}
