package com.dojo.challenge.imageprocessingservice.utils;

import com.dojo.challenge.imageprocessingservice.exceptions.custom.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidation {
    public static void validateFileExtension(MultipartFile file, List<String> allowedMimeTypes) throws BadRequestException {
        if(!allowedMimeTypes.contains(file.getContentType())) {
            throw new BadRequestException("file must be an image");
        }
    }
}
