package online.bottler.letter.application.dto;

import java.time.LocalDateTime;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;

public record LetterBoxDTO(Long userId, Long letterId, LetterType letterType, BoxType boxType,
                           LocalDateTime createdAt) {
    public static LetterBoxDTO of(Long userId, Long letterId,
                                  LetterType letterType, BoxType boxType,
                                  LocalDateTime createdAt) {
        return new LetterBoxDTO(userId, letterId, letterType, boxType, createdAt);
    }

    public LetterBox toDomain() {
        return LetterBox.builder()
                .userId(userId)
                .letterId(letterId)
                .letterType(letterType)
                .boxType(boxType)
                .createdAt(createdAt)
                .build();
    }
}
