package com.dojo.challenge.imageprocessingservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@Builder
public class ProcessImageRequest {
    @Id
    private String guid;
    private String status;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
}
