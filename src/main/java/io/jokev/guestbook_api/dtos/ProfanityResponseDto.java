package io.jokev.guestbook_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProfanityResponseDto {

    boolean isProfanity;

    float score;

    String flaggedFor;
}
