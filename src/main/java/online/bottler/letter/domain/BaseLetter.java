package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

public abstract class BaseLetter {
    @Getter
    protected Long id;
    @Getter
    protected final Long userId;
    protected final LetterContent letterContent;
    @Getter
    protected final boolean isDeleted;
    @Getter
    protected final boolean isBlocked;
    @Getter
    protected final LocalDateTime createdAt;

    protected BaseLetter(Long id, Long userId, LetterContent letterContent, boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.letterContent = letterContent;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
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
}
