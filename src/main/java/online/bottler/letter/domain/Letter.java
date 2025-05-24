package online.bottler.letter.domain;

import java.time.LocalDateTime;


public class Letter extends BaseLetter{
    private Letter(Long id, Long userId, LetterContent letterContent, boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        super(id, userId, letterContent, isDeleted, isBlocked, createdAt);
    }

    public static Letter of(Long id, Long userId, LetterContent letterContent, boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        return new Letter(id, userId, letterContent, isDeleted, isBlocked, createdAt);
    }

    public static Letter create(Long userId, LetterContent letterContent) {
        return new Letter(null, userId, letterContent, false, false, LocalDateTime.now());
    }
}
