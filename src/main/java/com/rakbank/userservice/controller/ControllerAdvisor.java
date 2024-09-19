package com.rakbank.userservice.controller;

import com.rakbank.userservice.controller.dto.response.ErrorResponse;
import com.rakbank.userservice.error.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleCheckedExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorResponse> handleUnCheckedExceptions(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(DomainException.class)
    public final ResponseEntity<ErrorResponse> handleDomainError(DomainException ex, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(getErrorResponse(ex), ex.getHttpStatus());
    }

    @ResponseBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Set<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(x -> buildExceptionMessage(x.getDefaultMessage(),null))
            .collect(Collectors.toSet());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .errorMessage(String.join(",", errors)).build();
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse getErrorResponse(DomainException ex) {
        String errorMessage = null;
        if(ex.getMessage() == null || ex.getMessage().trim().length() == 0) {
            errorMessage =  "";
        } else {
            errorMessage = buildExceptionMessage(ex.getMessage(), ex.getParams());
        }
        return ErrorResponse.builder()
                .errorMessage(errorMessage)
                .build();
    }

    private String buildExceptionMessage(String key, List<String> params) {
        String message = null;
        try{
            message = messageSource.getMessage(key,
                null != params ? params.toArray():null, LocaleContextHolder.getLocale());
        }catch(Exception exc){
            // in worst case if there is no key found in resource file, the message will be same as key
            message = key;
        }
        return message;
    }



}
