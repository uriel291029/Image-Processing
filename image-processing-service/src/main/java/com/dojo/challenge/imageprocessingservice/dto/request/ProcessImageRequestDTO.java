package com.dojo.challenge.imageprocessingservice.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ProcessImageRequestDTO {

    @NotBlank(message = "The title must not be empty, blank or null.")
    private String title;

    @NotBlank(message = "The description must not be empty, blank or null.")
    private String description;

    @NotNull(message = "The list of emails must not be null.")
    @Size(min = 1, message = "The list of emails must have at least one element.")
    private List<String> globantEmails;

    @NotBlank(message = "The folder name must not be empty, blank or null.")
    private String folderName;
}
