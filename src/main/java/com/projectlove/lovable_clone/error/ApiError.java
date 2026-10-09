package com.projectlove.lovable_clone.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.prefs.AbstractPreferences;

public record ApiError(
        HttpStatus status,
        String message,
        Instant timestamp,
       @JsonInclude(JsonInclude.Include.NON_NULL) List<ApiFieldError> errors
) {
    public ApiError(HttpStatus status,String message){
        this(status,message,Instant.now(),null);
    }
    public ApiError(HttpStatus status, String message, List<ApiFieldError> errors){
        this(status,message,Instant.now(),errors);
    }
}

record ApiFieldError(String field,String message){}
