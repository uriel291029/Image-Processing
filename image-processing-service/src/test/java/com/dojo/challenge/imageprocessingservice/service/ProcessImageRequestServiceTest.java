package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.dto.SearchProcessResponseDTO;
import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.enums.RequestStatus;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BadRequestException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BusinessException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.ResourceNotFoundException;
import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import com.dojo.challenge.imageprocessingservice.objectMother.ProcessImageRequestDTOMother;
import com.dojo.challenge.imageprocessingservice.objectMother.ProcessImageRequestMother;
import com.dojo.challenge.imageprocessingservice.repository.ProcessImageRequestRepository;
import com.dojo.challenge.imageprocessingservice.service.impl.ProcessImageRequestServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessImageRequestServiceTest {

    @InjectMocks
    ProcessImageRequestServiceImpl processImageRequestService;

    @Mock
    private ProcessImageRequestRepository processImageRequestRepository;


    @Mock
    private ProcessImageService uploadImageService;


    @BeforeEach
    public void init() {
        List<String> allowedFileExtensions = Arrays.asList("image/png","image/jpeg");
        ReflectionTestUtils.setField(processImageRequestService, "allowedFileExtensions", allowedFileExtensions);
    }

    @Test
    public void shouldGetStatusWhenRequestExists() {
        String guid = "123abc";

        ProcessImageRequest processImageRequest = ProcessImageRequestMother.create(
                RequestStatus.COMPLETED.getValue(),
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().minusMinutes(1),
                guid);

        when(processImageRequestRepository.findById(eq(guid))).thenReturn(Optional.of(processImageRequest));

        String result = processImageRequestService.getImageRequestStatus(guid);

        Assertions.assertThat(result).isNotBlank().isEqualTo(RequestStatus.COMPLETED.getValue());

        verify(processImageRequestRepository).findById(eq(guid));
    }

    @Test
    public void shouldFailWhenRequestDoesNotExists() {
        String guid = "123abc";

        when(processImageRequestRepository.findById(eq(guid))).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> processImageRequestService.getImageRequestStatus(guid))
                .hasMessage("process not found")
                .isExactlyInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void shouldGetGuidWhenRequestIsSuccessful() {

        MockMultipartFile image = new MockMultipartFile("data",
                "filename.png", "image/png", "some image".getBytes());

        ProcessImageRequestDTO requestInformation = ProcessImageRequestDTOMother.create(
                "anyTitle",
                "anyDescription",
                Arrays.asList("any@globant.com"),
                "any/folder"
        );

        String guid = "123abc";

        ProcessImageRequest processImageRequest = ProcessImageRequestMother.create(
                RequestStatus.COMPLETED.getValue(),
                LocalDateTime.now(),
                null,
                guid);


        when(processImageRequestRepository.insert(any(ProcessImageRequest.class)))
                .thenReturn(processImageRequest);

        String result = processImageRequestService.processImageRequest(requestInformation, image);

        Assertions.assertThat(result).isNotBlank().isEqualTo(guid);


        verify(processImageRequestRepository).insert(any(ProcessImageRequest.class));
        verify(uploadImageService).processImage(any(MultipartFile.class), any(ProcessImageRequestDTO.class),
                any(ByteArrayOutputStream.class), any(ProcessImageRequest.class));

    }

    @Test
    public void shouldFailWhenFileHasInvalidExtension() {
        ProcessImageRequestDTO requestInformation = ProcessImageRequestDTOMother.create(
                "anyTitle",
                "anyDescription",
                Arrays.asList("any@globant.com"),
                "any/folder"
        );

        MockMultipartFile image = new MockMultipartFile("data",
                "filename.png", "text", "some image".getBytes());

        Assertions.assertThatThrownBy(() -> processImageRequestService.processImageRequest(requestInformation, image))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage("file must be an image");
    }

    @Test
    public void shouldFailWhenUploadingFile() {
        MockMultipartFile image = new MockMultipartFile("data",
                "filename.png", "image/png", "some image".getBytes());

        ProcessImageRequestDTO requestInformation = ProcessImageRequestDTOMother.create(
                "anyTitle",
                "anyDescription",
                Arrays.asList("any@globant.com"),
                "any/folder"
        );

        String guid = "123abc";

        ProcessImageRequest processImageRequest = ProcessImageRequestMother.create(
                RequestStatus.COMPLETED.getValue(),
                LocalDateTime.now(),
                null,
                guid);


        when(processImageRequestRepository.insert(any(ProcessImageRequest.class)))
                .thenReturn(processImageRequest);

        doThrow(new BusinessException("error uploading file"))
                .when(uploadImageService).processImage(any(MultipartFile.class), any(ProcessImageRequestDTO.class),
                        any(ByteArrayOutputStream.class), any(ProcessImageRequest.class));



        Assertions.assertThatThrownBy(() -> processImageRequestService.processImageRequest(requestInformation, image))
                .isExactlyInstanceOf(BusinessException.class)
                .hasMessage("error uploading file");

        verify(processImageRequestRepository).insert(any(ProcessImageRequest.class));
        verify(processImageRequestRepository).save(any(ProcessImageRequest.class));
    }

    @Test
    public void shoulGetProcessList() {
    	String startDate = "2023-03-21";
    	String endDate = "2023-03-22";
    	LocalDate fromDate = LocalDate.parse(startDate);
    	LocalDate toDate = LocalDate.parse(endDate);
    	List<SearchProcessResponseDTO> list = new ArrayList<SearchProcessResponseDTO>();
    	SearchProcessResponseDTO element = new SearchProcessResponseDTO(RequestStatus.COMPLETED.getValue(), LocalDateTime.now(), LocalDateTime.now());
    	list.add(element);

    	ProcessImageRequest processImageRequest = ProcessImageRequestMother.create(
    			RequestStatus.COMPLETED.getValue(),
    			LocalDateTime.now().minusMinutes(5),
    			LocalDateTime.now().minusMinutes(1),
    			"123abc");
    	List<ProcessImageRequest> processList = new ArrayList<ProcessImageRequest>();
    	processList.add(processImageRequest);

    	when(processImageRequestRepository.findByBeginDateBetween(fromDate, toDate)).thenReturn(processList);
    	List<SearchProcessResponseDTO> result = processImageRequestService.searchProcess(startDate, endDate);

    	assertNotNull(result);
    	assertEquals(result.get(0).getStatus(), list.get(0).getStatus());
    }

    @Test
    public void shouldFailWhenIncorrectDateRange() {
    	String startDate = "2023-06-21";
    	String endDate = "2023-03-22";

    	Assertions.assertThatThrownBy(() -> processImageRequestService.searchProcess(startDate, endDate))
    	.hasMessage("The start date is greater than the end date")
    	.isExactlyInstanceOf(BusinessException.class);

    }
    
    @Test
    public void shouldFailWhenInvalidDate() {
    	String startDate = "2023-06-2";
    	String endDate = "2023-03-22";

    	Assertions.assertThatThrownBy(() -> processImageRequestService.searchProcess(startDate, endDate))
    	.hasMessage("Invalid date, the format must be be yyyy-mm-dd")
    	.isExactlyInstanceOf(BadRequestException.class);

    }
}
