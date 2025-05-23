package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

public abstract class BaseLetter {
    @Getter
    private Long id;
    @Getter
    private final Long userId;
    private final LetterContent letterContent;
    @Getter
    private final boolean isDeleted;
    @Getter
    private final boolean isBlocked;
    @Getter
    private final LocalDateTime createdAt;

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
