package io.jokev.guestbook_api.controller;

import io.jokev.guestbook_api.constants.AppConstants;
import io.jokev.guestbook_api.dtos.CreateGuestbookEntryDto;
import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.exceptions.GuestbookEntryValidationException;
import io.jokev.guestbook_api.exceptions.ProfanityViolationException;
import io.jokev.guestbook_api.models.GuestbookEntry;
import io.jokev.guestbook_api.services.GuestbookEntryService;
import io.jokev.guestbook_api.services.ProfanityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GuestbookEntryControllerTest {

    private GuestbookEntryService guestbookEntryService;
    private ProfanityService profanityService;
    private GuestbookEntryController controller;

    @BeforeEach
    void setUp() {
        guestbookEntryService = mock(GuestbookEntryService.class);
        profanityService = mock(ProfanityService.class);
        controller = new GuestbookEntryController(guestbookEntryService, profanityService);
    }

    @Test
    void getAllGuestbookEntriesReturnsPagedModel() {
        GuestbookEntry entry = GuestbookEntry.builder()
                .id(1L)
                .author("jokvn")
                .message("Hello world!")
                .build();

        Page<GuestbookEntry> page = new PageImpl<>(List.of(entry), PageRequest.of(0, 10), 1);
        when(guestbookEntryService.findAllEntries(any())).thenReturn(page);

        PagedModel<GuestbookEntryDto> result = controller.getAllGuestbookEntries(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("jokvn", result.getContent().get(0).getAuthor());
    }

    @Test
    void createGuestbookEntryReturnsCreatedEntry() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn")
                .message("Hello world!")
                .build();

        GuestbookEntryDto createdDto = GuestbookEntryDto.builder().author("jokvn").message("Hello world!").build();

        when(profanityService.isProfanity(anyString())).thenReturn(false);
        when(guestbookEntryService.createGuestbookEntry(any())).thenReturn(createdDto);

        ResponseEntity<GuestbookEntryDto> response = controller.createGuestbookEntry(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("jokvn", response.getBody().getAuthor());
    }

    @Test
    void createGuestbookEntryThrowsValidationExceptionForLongAuthor() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("A".repeat(AppConstants.GUESTBOOK_AUTHOR_LENGTH + 1))
                .message("Hello world!")
                .build();

        assertThrows(GuestbookEntryValidationException.class, () -> controller.createGuestbookEntry(dto));
    }

    @Test
    void createGuestbookEntryThrowsValidationExceptionForLongMessage() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn")
                .message("A".repeat(AppConstants.GUESTBOOK_MESSAGE_LENGTH + 1))
                .build();

        assertThrows(GuestbookEntryValidationException.class, () -> controller.createGuestbookEntry(dto));
    }

    @Test
    void createGuestbookEntryThrowsValidationExceptionForUnsafeAuthor() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn$")
                .message("Hello world!")
                .build();

        assertThrows(GuestbookEntryValidationException.class, () -> controller.createGuestbookEntry(dto));
    }

    @Test
    void createGuestbookEntryThrowsValidationExceptionForUnsafeMessage() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn")
                .message("Hello world$")
                .build();

        assertThrows(GuestbookEntryValidationException.class, () -> controller.createGuestbookEntry(dto));
    }

    @Test
    void createGuestbookEntryThrowsProfanityViolationException() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn")
                .message("badword")
                .build();

        when(profanityService.isProfanity(anyString())).thenReturn(true);

        assertThrows(ProfanityViolationException.class, () -> controller.createGuestbookEntry(dto));
    }

    @Test
    void createGuestbookEntrySetsAnonymousIfAuthorIsEmpty() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("")
                .message("Hello world!")
                .build();

        GuestbookEntryDto createdDto = GuestbookEntryDto.builder().author("anonymous").message("Hello world!").build();

        when(profanityService.isProfanity(anyString())).thenReturn(false);
        when(guestbookEntryService.createGuestbookEntry(any())).thenReturn(createdDto);

        ResponseEntity<GuestbookEntryDto> response = controller.createGuestbookEntry(dto);

        assertEquals("anonymous", response.getBody().getAuthor());
    }
}