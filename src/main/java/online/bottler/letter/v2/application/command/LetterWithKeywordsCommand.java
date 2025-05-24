package online.bottler.letter.v2.application.command;

import java.util.List;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterContent;

public record LetterWithKeywordsCommand(Letter letter, List<String> keywords) {

    public static LetterWithKeywordsCommand of(Long userId, String title, String content, String font, String paper,
                                               String label, List<String> keywords) {
        return new LetterWithKeywordsCommand(
                Letter.create(userId, LetterContent.of(title, content, font, paper, label)),
                keywords
        );
    }

    public Letter toLetter() {
        return letter;
    }

    public List<String> toKeywords() {
        return keywords;
    }
}
