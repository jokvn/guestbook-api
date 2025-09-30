package io.jokev.guestbook_api.service;

import io.jokev.guestbook_api.dtos.ProfanityResponseDto;
import io.jokev.guestbook_api.exceptions.ProfanityViolationException;
import io.jokev.guestbook_api.services.ProfanityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfanityServiceTest {

    private RestTemplate restTemplate;
    private ProfanityServiceImpl profanityService;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        profanityService = new ProfanityServiceImpl(restTemplate);
    }

    @Test
    public void testIsProfanityReturnsTrueWhenProfanityDetected() {
        ProfanityResponseDto responseDto = ProfanityResponseDto.builder().build();
        responseDto.setProfanity(true);

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(ProfanityResponseDto.class)))
                .thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        assertTrue(profanityService.isProfanity("badword"));
    }

    @Test
    public void testIsProfanityReturnsFalseWhenNoProfanityDetected() {
        ProfanityResponseDto responseDto = ProfanityResponseDto.builder().isProfanity(false).build();

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(ProfanityResponseDto.class)))
                .thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        assertFalse(profanityService.isProfanity("hello world"));
    }

    @Test
    public void testIsProfanityThrowsExceptionWhenApiReturnsNon2xx() {
        ProfanityResponseDto responseDto = ProfanityResponseDto.builder()
                .isProfanity(false)
                .build();

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(ProfanityResponseDto.class)))
                .thenReturn(new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST));

        assertThrows(ProfanityViolationException.class, () -> profanityService.isProfanity("test"));
    }

    @Test
    public void testIsProfanityThrowsExceptionWhenResponseBodyIsNull() {
        ResponseEntity<ProfanityResponseDto> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(ProfanityResponseDto.class)))
                .thenReturn(responseEntity);

        assertThrows(ProfanityViolationException.class, () -> profanityService.isProfanity("test"));
    }

    @Test
    public void testIsProfanityThrowsExceptionWhenRestClientException() {
        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(ProfanityResponseDto.class)))
                .thenThrow(new RestClientException("Connection error"));

        assertThrows(ProfanityViolationException.class, () -> profanityService.isProfanity("test"));
    }
}
