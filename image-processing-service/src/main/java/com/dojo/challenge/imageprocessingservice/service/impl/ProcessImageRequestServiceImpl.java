package com.dojo.challenge.imageprocessingservice.service.impl;

import com.dojo.challenge.imageprocessingservice.dto.SearchProcessResponseDTO;
import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.enums.RequestStatus;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BadRequestException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BusinessException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.ResourceNotFoundException;
import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import com.dojo.challenge.imageprocessingservice.repository.ProcessImageRequestRepository;
import com.dojo.challenge.imageprocessingservice.service.ProcessImageRequestService;
import com.dojo.challenge.imageprocessingservice.service.ProcessImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dojo.challenge.imageprocessingservice.utils.FileValidation.validateFileExtension;
import static com.dojo.challenge.imageprocessingservice.utils.DateValidator.validateRangeDate;
import static com.dojo.challenge.imageprocessingservice.utils.DateValidator.validateDateFormat;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessImageRequestServiceImpl implements ProcessImageRequestService {

    private final ProcessImageRequestRepository processImageRequestRepository;
    
    private final ProcessImageService uploadImageService;

    @Value("${allowed.multipart.file.extensions}")
    private List<String> allowedFileExtensions;

    @Override
    public String processImageRequest(ProcessImageRequestDTO requestInformation, MultipartFile image) {

        validateFileExtension(image, allowedFileExtensions);

        String guid = UUID.randomUUID().toString();

        ProcessImageRequest processImageRequest = ProcessImageRequest.builder()
                .beginDate(LocalDateTime.now())
                .guid(guid)
                .status(RequestStatus.IN_PROGRESS.getValue())
                .build();

        processImageRequest = processImageRequestRepository.insert(processImageRequest);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(image.getBytes());
            uploadImageService.processImage(image,
                    requestInformation,
                    byteArrayOutputStream,
                    processImageRequest);
        } catch (Exception exception) {
            log.error("It has occurred an error message : {}", exception.getMessage());
            processImageRequest.setStatus(RequestStatus.FAILED.getValue());
            processImageRequest.setEndDate(LocalDateTime.now());
            processImageRequestRepository.save(processImageRequest);
            log.error("Process image request with id : {} was set with status : {}", processImageRequest.getGuid(),
                    processImageRequest.getStatus());
            throw new BusinessException(exception.getMessage());
        }

        return processImageRequest.getGuid();
    }
    
    @Override
    public List<SearchProcessResponseDTO> searchProcess(String startDate, String endDate) throws BadRequestException{
    	LocalDate fromDate = validateDateFormat(startDate);
    	LocalDate toDate = validateDateFormat(endDate);
    	validateRangeDate(fromDate,toDate);
    	List<ProcessImageRequest> resultList = processImageRequestRepository.findByBeginDateBetween(fromDate, toDate);
    	
    	List<SearchProcessResponseDTO> responseListDTO = resultList.stream()
    			.map(processImageRequest -> SearchProcessResponseDTO
    					.builder()
    					.status(processImageRequest.getStatus())
    					.startDateTime(processImageRequest.getBeginDate())
    					.EndDateTime(processImageRequest.getEndDate())
    					.build()).collect(Collectors.toList());
    	
    	return responseListDTO;
    }
    

    @Override
    public String getImageRequestStatus(String guid) {
        ProcessImageRequest processImageRequest = processImageRequestRepository.findById(guid)
                .orElseThrow(() -> new ResourceNotFoundException("process not found"));

        return processImageRequest.getStatus();
    }
}
