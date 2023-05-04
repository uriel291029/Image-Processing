package com.dojo.challenge.imageprocessingservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorMessage {
    private String error;
}
