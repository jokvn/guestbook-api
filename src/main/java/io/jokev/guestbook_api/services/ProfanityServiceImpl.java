package io.jokev.guestbook_api.services;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.jokev.guestbook_api.dtos.ProfanityResponseDto;
import io.jokev.guestbook_api.exceptions.ProfanityViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfanityServiceImpl implements ProfanityService {

    private final static String PROFANITY_CHECK_URL = "https://vector.profanity.dev";
    private final RestTemplate restTemplate;

    @Override
    public boolean isProfanity(String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(Map.of("message", text), headers);

        try {
            ResponseEntity<ProfanityResponseDto> response = restTemplate.postForEntity(
                    PROFANITY_CHECK_URL,
                    requestEntity,
                    ProfanityResponseDto.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ProfanityViolationException(
                        "The input could not be validated for profanity.");
            }

            ProfanityResponseDto profanityResponseDto = response.getBody();

            if (profanityResponseDto != null) {
                return profanityResponseDto.isProfanity();
            } else {
                throw new ProfanityViolationException(
                        "The input could not be validated for profanity.");
            }
        } catch (RestClientException e) {
            throw new ProfanityViolationException(
                    "The input could not be validated for profanity.");
        }
    }

}
