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
    public static LetterWithKeywordsDetailResponse of(LetterWithKeywords letterWithKeywords, String profile, boolean isOwner, boolean isReplied) {
        return new LetterWithKeywordsDetailResponse(
                letterWithKeywords.letterId(),
                letterWithKeywords.title(),
                letterWithKeywords.content(),
                letterWithKeywords.keywords(),
                letterWithKeywords.font(),
                letterWithKeywords.paper(),
                profile,
                letterWithKeywords.label(),
                isOwner,
                isReplied,
                letterWithKeywords.createdAt()
        );
    }
}
