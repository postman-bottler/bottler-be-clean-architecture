package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Letter extends BaseLetter {

    private final Long userId;

    private Letter(
            Long id,
            Long userId,
            LetterContent letterContent,
            LetterStatus status,
            LocalDateTime createdAt
    ) {
        super(id, letterContent, status, createdAt);
        this.userId = userId;
    }

    public static Letter of(
            Long id,
            Long userId,
            LetterContent letterContent,
            LetterStatus status,
            LocalDateTime createdAt
    ) {
        return new Letter(
                id,
                userId,
                letterContent,
                status,
                createdAt
        );
    }

    public static Letter create(Long userId, LetterContent letterContent) {
        return new Letter(
                null,
                userId,
                letterContent,
                LetterStatus.OPEN,
                null
        );
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}
