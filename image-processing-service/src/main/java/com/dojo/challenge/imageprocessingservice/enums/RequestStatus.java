package com.dojo.challenge.imageprocessingservice.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RequestStatus {
    IN_PROGRESS("InProgress"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private String value;

    public String getValue(){
        return value;
    }
}
