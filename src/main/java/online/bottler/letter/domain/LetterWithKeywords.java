package online.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LetterWithKeywords {
    private final Letter letter;
    private final List<String> keywords;

    public Long getLetterId() {
        return letter.getId();
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

    public LocalDateTime getCreatedAt() {
        return letter.getCreatedAt();
    }

    public static LetterWithKeywords from(Letter letter, List<LetterKeyword> letterKeywords) {
        List<String> keywords = letterKeywords.stream().map(LetterKeyword::toString).toList();
        return new LetterWithKeywords(letter, keywords);
    }
}
