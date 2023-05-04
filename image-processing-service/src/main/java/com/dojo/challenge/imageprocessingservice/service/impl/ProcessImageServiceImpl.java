package com.dojo.challenge.imageprocessingservice.service.impl;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.enums.RequestStatus;
import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import com.dojo.challenge.imageprocessingservice.repository.ProcessImageRequestRepository;
import com.dojo.challenge.imageprocessingservice.service.ProcessImageService;
import com.dojo.challenge.imageprocessingservice.service.impl.EmailServiceImpl;
import com.dojo.challenge.imageprocessingservice.service.impl.GoogleFileManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessImageServiceImpl implements ProcessImageService {

    private static final String PARENT_FOLDER = "GlobantBEChallenge";

    private final GoogleFileManagerServiceImpl googleFileManagerService;

    private final ProcessImageRequestRepository processImageRequestRepository;

    private final EmailServiceImpl emailService;

    @Async
    @Override
    public void processImage(MultipartFile file,
                             ProcessImageRequestDTO requestInformation,
                             ByteArrayOutputStream byteArrayOutputStream,
                             ProcessImageRequest processImageRequest) {
        try {
            log.info("Starting upload file to the google drive");
            String fileId = googleFileManagerService.uploadFile(file,
                    PARENT_FOLDER + "/" + requestInformation.getFolderName(),
                    byteArrayOutputStream.toByteArray(),
                    requestInformation);
            log.info("Ending upload file to the google drive with id : {}", fileId);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            googleFileManagerService.downloadFile(fileId, outputStream);
            ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
            log.info("Sending emails each user of the request.");
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            emailService.sendWithAttach("uriel.yanez@globant.com",
                    requestInformation.getGlobantEmails().toArray(new String[0]),
                    "Hackaton",
                    "This image was sent from a spring boot project",
                    requestInformation.getTitle() + "." + extension,
                    byteArrayResource);
            log.info("Finish to send emails each user of the request.");
            log.info("Saving the process image request with id : {} with status Complete.", processImageRequest.getGuid());
            processImageRequest.setStatus(RequestStatus.COMPLETED.getValue());
            processImageRequest.setEndDate(LocalDateTime.now());
            processImageRequestRepository.save(processImageRequest);
            log.info("Finish update the process image request with id : {} with status Complete.", processImageRequest.getGuid());
        } catch (Exception exception) {
            log.error("It has occurred an error message : {}", exception.getMessage());
            processImageRequest.setStatus(RequestStatus.FAILED.getValue());
            processImageRequest.setEndDate(LocalDateTime.now());
            processImageRequestRepository.save(processImageRequest);
            log.error("Process image request with id : {} was set with status : {}", processImageRequest.getGuid(),
                    processImageRequest.getStatus());
        }
    }
}
