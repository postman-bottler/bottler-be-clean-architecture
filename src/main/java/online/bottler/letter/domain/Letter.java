package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Letter extends BaseDomain {
    private final Long userId;
    private final LetterContent letterContent;
    private final LetterStatus letterStatus;

    public Letter(Long id, Long userId, LetterContent letterContent, LetterStatus letterStatus, LocalDateTime createdAt) {
        super(id, createdAt);
        this.userId = userId;
        this.letterContent = letterContent;
        this.letterStatus = letterStatus;
    }

    public static Letter of(Long id, Long userId, LetterContent letterContent, LetterStatus letterStatus, LocalDateTime createdAt) {
        return new Letter(id, userId, letterContent, letterStatus, createdAt);
    }

    public static Letter create(Long userId, LetterContent letterContent) {
        return new Letter(null, userId, letterContent, LetterStatus.create(false, false), LocalDateTime.now());
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public String getTitle() {
        return letterContent.getTitle();
    }

    public String getContent() {
        return letterContent.getContent();
    }

    public String getFont() {
        return letterContent.getFont();
    }

    public String getPaper() {
        return letterContent.getPaper();
    }

    public String getLabel() {
        return letterContent.getLabel();
    }

    public boolean isDeleted() {
        return letterStatus.isDeleted();
    }

    public boolean isBlocked() {
        return letterStatus.isBlocked();
    }
}
