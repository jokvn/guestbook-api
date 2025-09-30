package io.jokev.guestbook_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jokev.guestbook_api.dtos.ErrorResponseDto;

/**
 * Global exception handler
 */
@ControllerAdvice
public class GuestbookExceptionHandler {

    @ExceptionHandler(GuestbookEntryValidationException.class)
    ResponseEntity<ErrorResponseDto> guestbookEntryValidationException(GuestbookEntryValidationException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ProfanityViolationException.class)
    ResponseEntity<ErrorResponseDto> profanityViolationException(ProfanityViolationException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
