package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BusinessException;
import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import com.dojo.challenge.imageprocessingservice.objectMother.ProcessImageRequestDTOMother;
import com.dojo.challenge.imageprocessingservice.repository.ProcessImageRequestRepository;
import com.dojo.challenge.imageprocessingservice.service.impl.EmailServiceImpl;
import com.dojo.challenge.imageprocessingservice.service.impl.GoogleFileManagerServiceImpl;
import com.dojo.challenge.imageprocessingservice.service.impl.ProcessImageServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ProcessImageServiceUnitTest {

    @Mock
    private GoogleFileManagerServiceImpl googleFileManagerService;

    @Mock
    private ProcessImageRequestRepository processImageRequestRepository;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private ProcessImageServiceImpl processImageService;

    @Test
    public void testProcessImage(){
        ProcessImageRequestDTO requestInformation = ProcessImageRequestDTOMother.create(
                "anyTitle",
                "anyDescription",
                Arrays.asList("any@globant.com"),
                "any/folder"
        );

        MockMultipartFile image = new MockMultipartFile("data",
                "filename.png", "text", "some image".getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProcessImageRequest processImageRequest = ProcessImageRequest.builder().build();

        processImageService.processImage(image, requestInformation, byteArrayOutputStream, processImageRequest);
    }

    @Test
    public void shouldFailProcessImage(){
        ProcessImageRequestDTO requestInformation = ProcessImageRequestDTOMother.create(
                "anyTitle",
                "anyDescription",
                Arrays.asList("any@globant.com"),
                "any/folder"
        );

        MockMultipartFile image = new MockMultipartFile("data",
                "filename.png", "text", "some image".getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProcessImageRequest processImageRequest = ProcessImageRequest.builder().build();

        doThrow(new DataIntegrityViolationException("Database Exception."))
                .when(processImageRequestRepository).save(any(ProcessImageRequest.class));

        Assertions.assertThatThrownBy(() -> processImageService.processImage(image, requestInformation, byteArrayOutputStream, processImageRequest))
                .isExactlyInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Database Exception.");
    }
}
