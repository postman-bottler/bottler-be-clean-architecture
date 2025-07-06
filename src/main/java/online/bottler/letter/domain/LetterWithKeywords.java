package online.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

public class LetterWithKeywords {
    private final Letter letter;
    @Getter
    private final List<String> keywords;

    private LetterWithKeywords(Letter letter, List<String> keywords) {
        this.letter = letter;
        this.keywords = List.copyOf(Objects.requireNonNull(keywords, "keywords must not be null"));
    }

    public Long getLetterId() {
        return letter.getId();
    }

    public Long getUserId() {
        return letter.getUserId();
    }

    public String getTitle() {
        return letter.getTitle();
    }

    public String getContent() {
        return letter.getContent();
    }

    public String getFont() {
        return letter.getFont();
    }

    public String getPaper() {
        return letter.getPaper();
    }

    public String getLabel() {
        return letter.getLabel();
    }

    public boolean isOwner(Long userId) {
        return letter.isOwner(userId);
    }

    public boolean isDeleted() {
        return letter.isDeleted();
    }

    public boolean isBlocked() {
        return letter.isBlocked();
    }

    public LocalDateTime getCreatedAt() {
        return letter.getCreatedAt();
    }

    public static LetterWithKeywords create(Letter letter, List<String> keywords) {
        return new LetterWithKeywords(letter, keywords);
    }
}
