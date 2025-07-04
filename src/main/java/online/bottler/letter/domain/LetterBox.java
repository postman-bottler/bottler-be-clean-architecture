package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterBox extends BaseDomain {
    private final Long userId;
    private final Long letterId;
    private final LetterBoxType letterBoxType;

    private LetterBox(Long id, Long userId, Long letterId, LetterBoxType letterBoxType, LocalDateTime createdAt) {
        super(id, createdAt);
        this.letterId = letterId;
        this.userId = userId;
        this.letterBoxType = letterBoxType;
    }

    public static LetterBox of(Long id, Long userId, Long letterId, LetterBoxType letterBoxType,
                               LocalDateTime createdAt) {
        return new LetterBox(id, userId, letterId, letterBoxType, createdAt);
    }

    public static LetterBox create(Long userId, Long letterId, LetterBoxType letterBoxType) {
        return new LetterBox(null, userId, letterId, letterBoxType, null);
    }
}
