package io.jokev.guestbook_api.mappings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.models.GuestbookEntry;

public class GuestbookEntryMapperTest {

    @Test
    public void testToDtoShouldMapEntityToDto() {
        GuestbookEntry entry = GuestbookEntry.builder()
                .id(1L)
                .author("jokvn")
                .message("Hello world!")
                .build();

        GuestbookEntryDto entryDto = GuestbookEntryDto.builder()
                .id(1L)
                .author("jokvn")
                .message("Hello world!")
                .build();

        assertNotNull(GuestbookEntryMapper.toDto(entry));
        assertEquals(GuestbookEntryMapper.toDto(entry), entryDto);
    }

    @Test
    public void testToDtoShouldReturnNullWhenEntityIsNull() {
        assertEquals(null, GuestbookEntryMapper.toDto(null));
    }
}
