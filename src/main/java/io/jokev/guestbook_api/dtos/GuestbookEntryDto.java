package io.jokev.guestbook_api.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GuestbookEntryDto {

    private long id;

    private String author;

    private String message;

    private LocalDateTime createdAt;

}
