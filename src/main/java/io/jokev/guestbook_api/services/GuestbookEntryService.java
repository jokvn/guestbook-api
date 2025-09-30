package io.jokev.guestbook_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.jokev.guestbook_api.dtos.CreateGuestbookEntryDto;
import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.models.GuestbookEntry;

public interface GuestbookEntryService {

    GuestbookEntryDto createGuestbookEntry(CreateGuestbookEntryDto createGuestbookEntryDto);

    Page<GuestbookEntry> findAllEntries(Pageable pageable);

}