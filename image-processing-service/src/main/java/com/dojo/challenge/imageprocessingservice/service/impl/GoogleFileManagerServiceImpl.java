package com.dojo.challenge.imageprocessingservice.service.impl;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.service.GoogleDriveManagerService;
import com.dojo.challenge.imageprocessingservice.service.GoogleFileManagerService;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleFileManagerServiceImpl implements GoogleFileManagerService {

    private final GoogleDriveManagerService googleDriveManagerService;

    @Override
    public String getFolderId(String path) throws Exception {
        log.info("Getting the folder id from the path: {}", path);
        String parentId = null;
        String[] folderNames = path.split("/");

        Drive driveInstance = googleDriveManagerService.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    @Override
    public String uploadFile(MultipartFile file,
                             String filePath,
                             byte[] fileBytes,
                             ProcessImageRequestDTO requestInformation) throws Exception {
        log.info("Uploading the file with request information : {}", requestInformation);
        try {
            String folderId = getFolderId(filePath);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(requestInformation.getTitle());
                fileMetadata.setDescription(requestInformation.getDescription());
                File uploadFile = googleDriveManagerService.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(fileBytes))
                        )
                        .setFields("id").execute();
                return uploadFile.getId();
            }
        } catch (Exception exception) {
            log.error("Error: {}", exception.getMessage());
            throw exception;
        }
        return null;
    }

    @Override
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        log.info("Download the file with id: {}", id);
        if (id != null) {
            googleDriveManagerService.getInstance().files().get(id).executeMediaAndDownloadTo(outputStream);
        }
    }
    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);

        if (folderId != null) {
            log.info("Folder already exists, so return id : {}", folderId);
            return folderId;
        }

        log.info("Folder don't exists, create it and return folderId");
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result;

        log.info("Searching the folder by the parentId : {} and folderName :  {}", parentId, folderName);
        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }
}
