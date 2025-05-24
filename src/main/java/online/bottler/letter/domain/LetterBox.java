package online.bottler.letter.domain;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterBox {
    private Long id;
    private final Long userId;
    private final Long letterId;
    private final LetterType letterType;
    private final BoxType boxType;
    private final LocalDateTime createdAt;

    private LetterBox(Long id, Long userId, Long letterId, LetterType letterType, BoxType boxType,
                      LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.letterId = letterId;
        this.letterType = letterType;
        this.boxType = boxType;
        this.createdAt = createdAt;
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