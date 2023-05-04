package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.dto.SearchProcessResponseDTO;
import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface ProcessImageRequestService {
	
    String processImageRequest(ProcessImageRequestDTO requestInformation, MultipartFile image);
    List<SearchProcessResponseDTO> searchProcess(String startDate, String endDate);
    String getImageRequestStatus(String guid);
}
