package io.jokev.guestbook_api.exceptions;

public class GuestbookEntryValidationException extends RuntimeException {

    public GuestbookEntryValidationException(String message) {
        super(message);
    }

    public GuestbookEntryValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
