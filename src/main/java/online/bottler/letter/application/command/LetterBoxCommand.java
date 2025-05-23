package online.bottler.letter.application.command;

import java.time.LocalDateTime;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;

public record LetterBoxCommand(Long userId, Long letterId, LetterType letterType, BoxType boxType,
                               LocalDateTime createdAt) {
    public static LetterBoxCommand of(Long userId, Long letterId,
                                      LetterType letterType, BoxType boxType,
                                      LocalDateTime createdAt) {
        return new LetterBoxCommand(userId, letterId, letterType, boxType, createdAt);
    }

    public LetterBox toDomain() {
        return LetterBox.create(userId, letterId, letterType, boxType, createdAt);
    }
}
