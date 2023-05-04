package com.dojo.challenge.imageprocessingservice.objectMother;

import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ProcessImageRequestDTOMother {
    public ProcessImageRequestDTO create(
            String title,
            String description,
            List<String> globantEmails,
            String folderName
    ) {
        return ProcessImageRequestDTO.builder()
                .title(title)
                .description(description)
                .globantEmails(globantEmails)
                .folderName(folderName)
                .build();
    }
}
