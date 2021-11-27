package com.glofox.book.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.glofox.book.exception.NotFoundException;
import com.glofox.book.exception.ServiceException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        return sendError(e);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(ServiceException e) {
        return sendError(e.getCause());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return sendError(ex, ex.getBindingResult().getAllErrors(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return sendError(ex, null, BAD_REQUEST);
    }

    private ResponseEntity<?> sendError(final Throwable ex) {
        return sendError(ex, ofNullable(statusErrorsMap.get(ex.getClass()))
                .orElse(INTERNAL_SERVER_ERROR));
    }

    private ResponseEntity<?> sendError(final Throwable ex, final HttpStatus status) {

        return sendError(ex, null, status);
    }

    private ResponseEntity<Object> sendError(
            final Throwable ex, final List<?> errors,
            final HttpStatus status) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .errors(errors)
                .build(), status);
    }

    @Getter
    @Setter
    @Builder
    private static class ErrorResponse {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<?> errors;

    }

    private static final Map<Class<? extends Exception>, HttpStatus> statusErrorsMap = new HashMap<>();

    static {
        statusErrorsMap.put(NotFoundException.class, NOT_FOUND);
        statusErrorsMap.put(InvalidParameterException.class, BAD_REQUEST);
    }

}
