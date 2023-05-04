package com.dojo.challenge.imageprocessingservice.exceptions;

import com.dojo.challenge.imageprocessingservice.dto.ErrorMessage;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BadRequestException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BusinessException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorMessage> handleBadRequestException(Exception e) {
        ErrorMessage error = ErrorMessage.builder().error(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorMessage> handleBusinessException(Exception e) {
        ErrorMessage error = ErrorMessage.builder().error(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorMessage> handleResourceNotFoundException(Exception e) {
        ErrorMessage error = ErrorMessage.builder().error(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorMessage> handleBindException(
            BindException ex) {
        ErrorMessage error = ErrorMessage.builder().error(ex.getFieldErrors().get(0).getDefaultMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorMessage> handleException(Exception e) {
        ErrorMessage error = ErrorMessage.builder().error(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
