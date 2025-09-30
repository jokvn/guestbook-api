package io.jokev.guestbook_api.dtos;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import io.jokev.guestbook_api.constants.AppConstants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

    @Builder.Default
    private String timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(AppConstants.RFC_3339_MICRO);

    private int status;
    private String error;
    private String message;
}
