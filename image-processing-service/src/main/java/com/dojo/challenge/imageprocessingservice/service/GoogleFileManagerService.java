package com.dojo.challenge.imageprocessingservice.service;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

public interface GoogleFileManagerService {

    String getFolderId(String path) throws Exception;
    String uploadFile(MultipartFile file,
                             String filePath,
                             byte[] fileBytes,
                             ProcessImageRequestDTO requestInformation) throws Exception;

   void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException;
}
