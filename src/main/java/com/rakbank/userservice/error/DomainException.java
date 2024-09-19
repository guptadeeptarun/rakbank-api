package com.rakbank.userservice.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class DomainException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
    private List<String> params;

    public DomainException(String message, List params, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.params = params;
        this.message = message;
    }

    public DomainException(String message) {
        this(message, null, HttpStatus.BAD_REQUEST);
    }

    public DomainException(String message, List<String> params) {
        this(message, params, HttpStatus.BAD_REQUEST);
    }

    public DomainException(String message, HttpStatus httpStatus) {
        this(message, null, httpStatus);
    }




}
