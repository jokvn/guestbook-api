package io.jokev.guestbook_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.jokev.guestbook_api.dtos.CreateGuestbookEntryDto;
import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.mappings.GuestbookEntryMapper;
import io.jokev.guestbook_api.models.GuestbookEntry;
import io.jokev.guestbook_api.repos.GuestbookEntryRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestbookEntryServiceImpl implements GuestbookEntryService {

    private final GuestbookEntryRepo guestbookEntryRepo;

    @Override
    public GuestbookEntryDto createGuestbookEntry(CreateGuestbookEntryDto createGuestbookEntryDto) {
        GuestbookEntry guestbookEntry = GuestbookEntry.builder()
                .author(createGuestbookEntryDto.getAuthor())
                .message(createGuestbookEntryDto.getMessage())
                .build();

        GuestbookEntry createdEntry = guestbookEntryRepo.save(guestbookEntry);
        return GuestbookEntryMapper.toDto(createdEntry);
    }

    @Override
    public Page<GuestbookEntry> findAllEntries(Pageable pageable) {
        return guestbookEntryRepo.findAll(pageable);
    }
}
