package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
abstract class BaseLetter extends BaseDomain {
    private final LetterContent letterContent;
    private LetterStatus status;

    protected BaseLetter(
            Long id,
            LetterContent letterContent,
            LetterStatus status,
            LocalDateTime created_at
    ) {
        super(id, created_at);
        this.letterContent = letterContent;
        this.status = status;
    }

    public void delete() {
        this.status = LetterStatus.DELETED;
    }

    public void block() {
        this.status = LetterStatus.BLOCKED;
    }

    public boolean isOpen() {
        return status == LetterStatus.OPEN;
    }

    public boolean isDeleted() {
        return status == LetterStatus.DELETED;
    }

    public boolean isBlocked() {
        return status == LetterStatus.BLOCKED;
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
