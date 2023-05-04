package com.dojo.challenge.imageprocessingservice.objectMother;

import com.dojo.challenge.imageprocessingservice.model.ProcessImageRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class ProcessImageRequestMother {

    public ProcessImageRequest create(
            String status,
            LocalDateTime beginDate,
            LocalDateTime endDate,
            String guid
    ) {
        return ProcessImageRequest.builder()
                .status(status)
                .beginDate(beginDate)
                .endDate(endDate)
                .guid(guid)
                .build();
    }

}
