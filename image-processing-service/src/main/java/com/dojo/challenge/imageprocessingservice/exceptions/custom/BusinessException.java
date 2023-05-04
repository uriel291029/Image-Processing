package com.dojo.challenge.imageprocessingservice.exceptions.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private String message;

    public BusinessException(String message){
        super(message);
        this.message = message;
    }
}
