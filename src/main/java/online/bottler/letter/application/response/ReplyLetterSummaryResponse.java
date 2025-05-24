package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.ReplyLetter;

public record ReplyLetterSummaryResponse(
        Long replyLetterId,
        String title,
        String label,
        LocalDateTime createdAt
) {
    public static ReplyLetterSummaryResponse from(ReplyLetter replyLetter) {
        return new ReplyLetterSummaryResponse(
                replyLetter.getId(),
                replyLetter.getTitle(),
                replyLetter.getLabel(),
                replyLetter.getCreatedAt()
        );
    }
}
