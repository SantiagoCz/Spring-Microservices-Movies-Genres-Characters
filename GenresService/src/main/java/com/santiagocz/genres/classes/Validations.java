package com.santiagocz.genres.classes;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;

public class Validations {

    public static ResponseEntity<?> handleValidationErrors(BindingResult result) {
        if (result.hasErrors()) {
            // If there are validation errors, build a detailed error message
            StringBuilder errorMessage = new StringBuilder("Validation error in the following fields:\n");
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorMessage.append("- ").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
            }
            // Return an error response with the validation details
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        return null; // No validation errors
    }

    public static <T> ResponseEntity<?> validateFields(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            // If there are validation errors, build a detailed error message
            StringBuilder errorMessage = new StringBuilder("Validation error in the following fields:\n");
            for (ConstraintViolation<T> violation : violations) {
                errorMessage.append("- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            // Return an error response with validation details
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        return null;
    }
}