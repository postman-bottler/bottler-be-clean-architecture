package online.bottler.letter.v2.application.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.ReplyLetter;

public record ReplyLetterDetailResponse(
        Long replyLetterId,
        String content,
        String font,
        String paper,
        String label,
        boolean isReplied,
        LocalDateTime createdAt
) {
    public static ReplyLetterDetailResponse from(ReplyLetter replyLetter, boolean isReplied) {
        return new ReplyLetterDetailResponse(
                replyLetter.getId(),
                replyLetter.getContent(),
                replyLetter.getFont(),
                replyLetter.getPaper(),
                replyLetter.getLabel(),
                isReplied,
                replyLetter.getCreatedAt()
        );
    }
}
