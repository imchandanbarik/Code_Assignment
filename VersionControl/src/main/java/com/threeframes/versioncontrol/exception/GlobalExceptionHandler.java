package com.threeframes.versioncontrol.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> rootExceptionHandlerForAllException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(20, 2001, e.getMessage(), "Exception Occurred at root");
        return new ResponseEntity<ErrorMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorMessage> badRequestException(BadRequestException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Bad Request");
        errorMessage.setErrDescription("Needed Data is not found");
        if (!ex.getErrmessage().isEmpty()) {
            errorMessage.setErrMessage(ex.getErrmessage());
        }
        if(!ex.getErrDescription().isEmpty()) {
            errorMessage.setErrDescription(ex.getErrDescription());
        }
        errorMessage.setErrCode(15);
        errorMessage.setErrStatus(1502);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
