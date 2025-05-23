package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.Letter;

public record LetterResponse(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static LetterResponse from(Letter letter, List<LetterKeyword> letterKeywords) {
        return new LetterResponse(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letterKeywords.stream().map(LetterKeyword::getKeyword).toList(),
                letter.getFont(),
                letter.getPaper(),
                letter.getLabel(),
                letter.getCreatedAt()
        );
    }
}
