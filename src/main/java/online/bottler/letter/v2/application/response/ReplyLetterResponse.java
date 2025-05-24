package online.bottler.letter.v2.application.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.ReplyLetter;

public record ReplyLetterResponse(
        Long replyLetterId,
        String content,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static ReplyLetterResponse from(ReplyLetter replyLetter) {
        return new ReplyLetterResponse(
                replyLetter.getId(),
                replyLetter.getContent(),
                replyLetter.getFont(),
                replyLetter.getPaper(),
                replyLetter.getLabel(),
                replyLetter.getCreatedAt()
        );
    }
}
