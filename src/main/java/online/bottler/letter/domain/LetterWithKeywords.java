package online.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

public class LetterWithKeywords {
    private final Letter letter;
    @Getter
    private final List<String> keywords;

    private LetterWithKeywords(Letter letter, List<String> keywords) {
        this.letter = letter;
        this.keywords = keywords;
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

    public boolean getIsDeleted() {
        return letter.isDeleted;
    }

    public boolean getIsBlocked() {
        return letter.isBlocked;
    }

    public LocalDateTime getCreatedAt() {
        return letter.getCreatedAt();
    }

    public static LetterWithKeywords from(Letter letter, List<LetterKeyword> letterKeywords) {
        List<String> keywords = letterKeywords.stream().map(LetterKeyword::toString).toList();
        return new LetterWithKeywords(letter, keywords);
    }
}
