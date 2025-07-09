package online.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;

public record LetterWithKeywords(Letter letter, List<String> keywords) {

    public Long letterId() {
        return letter.getId();
    }

    public Long userId() {
        return letter.getUserId();
    }

    public String title() {
        return letter.getTitle();
    }

    public String content() {
        return letter.getContent();
    }

    public String font() {
        return letter.getFont();
    }

    public String paper() {
        return letter.getPaper();
    }

    public String label() {
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

    public LocalDateTime createdAt() {
        return letter.getCreatedAt();
    }

    public static LetterWithKeywords create(Letter letter, List<String> keywords) {
        return new LetterWithKeywords(letter, keywords);
    }
}
