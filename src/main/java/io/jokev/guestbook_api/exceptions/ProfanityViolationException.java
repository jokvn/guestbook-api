package io.jokev.guestbook_api.exceptions;

public class ProfanityViolationException extends RuntimeException {

    public ProfanityViolationException(String message) {
        super(message);
    }

    public ProfanityViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
