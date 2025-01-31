package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlePermissionDeniedException(final PermissionDeniedException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final BookingStatusConflictException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityAvailableException(final EntityAvailableException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAnnotationValidationErrors(final MethodArgumentNotValidException e) { // Ошибки валидации полей объектов
        log.error(e.getMessage());

        String[] errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> String.format("%s: %s", ((FieldError) err).getField(), err.getDefaultMessage()))
                .toArray(String[]::new);

        return new ErrorResponse(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationErrors(final jakarta.validation.ConstraintViolationException e) { // Ошибки валидации примитивов в аргументах методов
        log.error(e.getConstraintViolations().toString());

        String[] errors = e.getConstraintViolations().stream()
                .map(violation -> {
                    String property = violation.getPropertyPath().toString();
                    String lastPathSegment = property.substring(property.lastIndexOf('.') + 1);
                    return String.format("%s: %s", lastPathSegment, violation.getMessage());
                })
                .toArray(String[]::new);

        return new ErrorResponse(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) { // Ошибки БД
        if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();

            if ("users_email_key".equals(constraintName)) {
                return new ResponseEntity<>(new ErrorResponse("Email уже существует"), HttpStatus.CONFLICT);
            }
        }

        return new ResponseEntity<>(new ErrorResponse("Ошибка БД"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
