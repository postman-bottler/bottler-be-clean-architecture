package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public class RecommendedLetter {
    private Long id;
    private final Long userId;
    private final Long letterId;

    private RecommendedLetter(Long id, Long userId, Long letterId) {
        this.id = id;
        this.userId = userId;
        this.letterId = letterId;
    }

    public static RecommendedLetter of(Long id, Long userId, Long letterId) {
        return new RecommendedLetter(id, userId, letterId);
    }

    public static RecommendedLetter create(Long userId, Long letterId) {
        return new RecommendedLetter(null, userId, letterId);
    }
}
