package com.backend.simya.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ValidErrorDetails {
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validateDetails = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            log.info("field error: {}", error.getDefaultMessage());
            String validKeyName = String.format("valid_%s", error.getField());
            validateDetails.put(validKeyName, error.getDefaultMessage());
        }
        return validateDetails;
    }

}
