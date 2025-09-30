package io.jokev.guestbook_api.mappings;

import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.models.GuestbookEntry;

public class GuestbookEntryMapper {

    public static GuestbookEntryDto toDto(GuestbookEntry entity) {
        if (entity == null) {
            return null;
        }

        return GuestbookEntryDto.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
