package com.threeframes.versioncontrol.exception;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException{
    private String errDescription;
    private String errmessage;

    public BadRequestException(String message, String description) {
        this.errmessage = message;
        this.errDescription = description;
    }
}
