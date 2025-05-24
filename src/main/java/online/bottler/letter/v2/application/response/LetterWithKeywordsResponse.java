package online.bottler.letter.v2.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.LetterWithKeywords;

public record LetterWithKeywordsResponse(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static LetterWithKeywordsResponse from(LetterWithKeywords letterWithKeywords) {
        return new LetterWithKeywordsResponse(
                letterWithKeywords.getLetterId(),
                letterWithKeywords.getTitle(),
                letterWithKeywords.getContent(),
                letterWithKeywords.getKeywords(),
                letterWithKeywords.getFont(),
                letterWithKeywords.getPaper(),
                letterWithKeywords.getLabel(),
                letterWithKeywords.getCreatedAt()
        );
    }
}
