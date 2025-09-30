package io.jokev.guestbook_api.service;

import io.jokev.guestbook_api.dtos.CreateGuestbookEntryDto;
import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.mappings.GuestbookEntryMapper;
import io.jokev.guestbook_api.models.GuestbookEntry;
import io.jokev.guestbook_api.repos.GuestbookEntryRepo;
import io.jokev.guestbook_api.services.GuestbookEntryService;
import io.jokev.guestbook_api.services.GuestbookEntryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GuestbookEntryServiceTest {

    private GuestbookEntryRepo guestbookEntryRepo;
    private GuestbookEntryService service;

    @BeforeEach
    public void setUp() {
        guestbookEntryRepo = mock(GuestbookEntryRepo.class);
        service = new GuestbookEntryServiceImpl(guestbookEntryRepo);
    }

    @Test
    public void testFindAllEntriesReturnsPageFromRepo() {

        GuestbookEntry entry1 = GuestbookEntry.builder()
                .author("jokvn")
                .message("Hello!")
                .build();

        GuestbookEntry entry2 = GuestbookEntry.builder()
                .author("Terry Davis")
                .message("Hi!")
                .build();

        List<GuestbookEntry> entries = Arrays.asList(entry1, entry2);
        Page<GuestbookEntry> page = new PageImpl<>(entries);

        Pageable pageable = PageRequest.of(0, 10);
        when(guestbookEntryRepo.findAll(pageable)).thenReturn(page);

        Page<GuestbookEntry> result = service.findAllEntries(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("jokvn", result.getContent().get(0).getAuthor());
        assertEquals("Terry Davis", result.getContent().get(1).getAuthor());
        verify(guestbookEntryRepo, times(1)).findAll(pageable);
    }

    @Test
    public void testCreateGuestbookEntrySavesAndReturnsDto() {
        CreateGuestbookEntryDto dto = CreateGuestbookEntryDto.builder()
                .author("jokvn")
                .message("Hello World!")
                .build();

        GuestbookEntry savedEntry = GuestbookEntry.builder()
                .author(dto.getAuthor())
                .message(dto.getMessage())
                .build();

        when(guestbookEntryRepo.save(any(GuestbookEntry.class))).thenReturn(savedEntry);

        try (MockedStatic<GuestbookEntryMapper> mocked = Mockito.mockStatic(GuestbookEntryMapper.class)) {
            GuestbookEntryDto expectedDto = GuestbookEntryDto.builder()
                    .id(1L)
                    .author("jokvn")
                    .message("Hello World!")
                    .build();

            mocked.when(() -> GuestbookEntryMapper.toDto(savedEntry)).thenReturn(expectedDto);

            GuestbookEntryDto result = service.createGuestbookEntry(dto);

            assertEquals("jokvn", result.getAuthor());
            assertEquals("Hello World!", result.getMessage());
            verify(guestbookEntryRepo, times(1)).save(any(GuestbookEntry.class));
        }
    }
}