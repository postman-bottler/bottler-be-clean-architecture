package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.LetterWithKeywords;

public record LetterWithKeywordsDetailResponse(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String profile,
        String label,
        boolean isOwner,
        boolean isReplied,
        LocalDateTime createdAt
) {
    public static LetterWithKeywordsDetailResponse of(LetterWithKeywords letterWithKeywords, Long currentUserId,
                                                      String profile, boolean isReplied) {
        return new LetterWithKeywordsDetailResponse(
                letterWithKeywords.getLetterId(),
                letterWithKeywords.getTitle(),
                letterWithKeywords.getContent(),
                letterWithKeywords.getKeywords(),
                letterWithKeywords.getFont(),
                letterWithKeywords.getPaper(),
                profile,
                letterWithKeywords.getLabel(),
                letterWithKeywords.getUserId().equals(currentUserId),
                isReplied,
                letterWithKeywords.getCreatedAt()
        );
    }
}
