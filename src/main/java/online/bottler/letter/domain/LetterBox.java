package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterBox extends BaseDomain {
    private final Long userId;
    private final Long letterId;
    private final LetterType letterType;
    private final BoxType boxType;

    private LetterBox(Long id, Long userId, Long letterId, LetterType letterType, BoxType boxType,
                     LocalDateTime createdAt) {
        super(id, createdAt);
        this.userId = userId;
        this.letterId = letterId;
        this.letterType = letterType;
        this.boxType = boxType;
    }

    public static LetterBox of(Long id, Long userId, Long letterId, LetterType letterType, BoxType boxType,
                               LocalDateTime createdAt) {
        return new LetterBox(id, userId, letterId, letterType, boxType, createdAt);
    }

    public static LetterBox create(Long userId, Long letterId, LetterType letterType, BoxType boxType,
                                   LocalDateTime createdAt) {
        return new LetterBox(null, userId, letterId, letterType, boxType, createdAt);
    }
}
