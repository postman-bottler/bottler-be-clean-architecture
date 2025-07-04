package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LetterSummary {
    private final Long letterId;
    private final String title;
    private final String label;
    private final LetterType letterType;
    private final BoxType boxType;
    private final LocalDateTime createdAt;

    private LetterSummary(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                         LocalDateTime createdAt) {
        this.letterId = letterId;
        this.title = title;
        this.label = label;
        this.letterType = letterType;
        this.boxType = boxType;
        this.createdAt = createdAt;
    }

    public static LetterSummary of(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                                   LocalDateTime createdAt) {
        return new LetterSummary(letterId, title, label, letterType, boxType, createdAt);
    }
}
