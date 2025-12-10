package com.pethealthtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super("%s con %s '%s' ya existe".formatted(resourceName, fieldName, fieldValue));
    }
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
