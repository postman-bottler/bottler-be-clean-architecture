package online.bottler.letter.domain;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyLetter extends BaseLetter {
    private final Long letterId;
    private final Long receiverId;

    public ReplyLetter(Long id, Long userId, LetterContent letterContent, boolean isDeleted, boolean isBlocked,
                       Long letterId, Long receiverId, LocalDateTime createdAt) {
        super(id, userId, letterContent, isDeleted, isBlocked, createdAt);
        this.letterId = letterId;
        this.receiverId = receiverId;
    }

    public static ReplyLetter of(Long id, Long userId, LetterContent letterContent, boolean isDeleted, boolean isBlocked, Long letterId, Long receiverId, LocalDateTime createdAt) {
        return new ReplyLetter(id, userId, letterContent, isDeleted, isBlocked, letterId, receiverId, createdAt);
    }

    public static ReplyLetter create(Long userId, LetterContent letterContent, Long letterId, Long receiverId) {
        return new ReplyLetter(null, userId, letterContent, false, false, letterId, receiverId, LocalDateTime.now());
    }
}
