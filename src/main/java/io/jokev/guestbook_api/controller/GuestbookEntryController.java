package io.jokev.guestbook_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.jokev.guestbook_api.constants.ApiConstants;
import io.jokev.guestbook_api.constants.AppConstants;
import io.jokev.guestbook_api.dtos.CreateGuestbookEntryDto;
import io.jokev.guestbook_api.dtos.GuestbookEntryDto;
import io.jokev.guestbook_api.exceptions.GuestbookEntryValidationException;
import io.jokev.guestbook_api.exceptions.ProfanityViolationException;
import io.jokev.guestbook_api.mappings.GuestbookEntryMapper;
import io.jokev.guestbook_api.models.GuestbookEntry;
import io.jokev.guestbook_api.services.GuestbookEntryService;
import io.jokev.guestbook_api.services.ProfanityService;
import io.jokev.guestbook_api.util.SafeTextValidator;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiConstants.GLOBAL_REQUEST_MAPPING_PATH + "/"
        + ApiConstants.GUESTBOOK_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
public class GuestbookEntryController {

    private final GuestbookEntryService guestbookEntryService;
    private final ProfanityService profanityService;

    @GetMapping("/all")
    public PagedModel<GuestbookEntryDto> getAllGuestbookEntries(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GuestbookEntry> guestbookEntries = guestbookEntryService.findAllEntries(pageable);

        List<GuestbookEntryDto> dtoList = guestbookEntries
                .getContent()
                .stream()
                .map(entry -> GuestbookEntryMapper.toDto(entry))
                .toList();

        Page<GuestbookEntryDto> dtoPage = new PageImpl<>(dtoList, pageable, guestbookEntries.getTotalElements());

        return new PagedModel<>(dtoPage);
    }

    @PostMapping("/create")
    public ResponseEntity<GuestbookEntryDto> createGuestbookEntry(
            @RequestBody CreateGuestbookEntryDto createGuestbookEntryDto) {

        createGuestbookEntryDto.setAuthor(createGuestbookEntryDto.getAuthor().trim());
        createGuestbookEntryDto.setMessage(createGuestbookEntryDto.getMessage().trim());

        if (createGuestbookEntryDto.getAuthor().length() > AppConstants.GUESTBOOK_AUTHOR_LENGTH) {
            throw new GuestbookEntryValidationException(
                    "Name must be at most " + AppConstants.GUESTBOOK_AUTHOR_LENGTH + " characters long.");
        } else if (createGuestbookEntryDto.getAuthor().length() == 0) {
            createGuestbookEntryDto.setAuthor("anonymous");
        }

        if (createGuestbookEntryDto.getMessage().length() > AppConstants.GUESTBOOK_MESSAGE_LENGTH) {
            throw new GuestbookEntryValidationException(
                    "Message must be at most " + AppConstants.GUESTBOOK_MESSAGE_LENGTH + " characters long.");
        }

        if (!SafeTextValidator.isSafe(createGuestbookEntryDto.getAuthor())) {
            throw new GuestbookEntryValidationException("Name contains restricted characters.");
        }

        if (!SafeTextValidator.isSafe(createGuestbookEntryDto.getMessage())) {
            throw new GuestbookEntryValidationException("Message contains restricted characters.");
        }

        if (profanityService.isProfanity(createGuestbookEntryDto.getAuthor())
                || profanityService.isProfanity(createGuestbookEntryDto.getMessage())) {
            throw new ProfanityViolationException(
                    "The input contains language that does not comply with communication standards.");
        }

        GuestbookEntryDto createdGuestbookEntryDto = guestbookEntryService
                .createGuestbookEntry(createGuestbookEntryDto);

        return ResponseEntity.ok(createdGuestbookEntryDto);
    }
}
