package io.jokev.guestbook_api.constants;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Application Constants
 */
public class AppConstants {

    public static final DateTimeFormatter RFC_3339_MICRO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);
    public static final int GUESTBOOK_AUTHOR_LENGTH = 15;
    public static final int GUESTBOOK_MESSAGE_LENGTH = 200;

}