package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface ProcessImageService {

    void processImage(MultipartFile file,
                      ProcessImageRequestDTO requestInformation,
                      ByteArrayOutputStream byteArrayOutputStream,
                      ProcessImageRequest processImageRequest);
}
