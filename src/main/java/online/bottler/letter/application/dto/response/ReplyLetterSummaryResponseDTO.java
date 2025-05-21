package online.bottler.letter.application.dto.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.ReplyLetter;

public record ReplyLetterSummaryResponseDTO(
        Long replyLetterId,
        String title,
        String label,
        LocalDateTime createdAt
) {
    public static ReplyLetterSummaryResponseDTO from(ReplyLetter replyLetter) {
        return new ReplyLetterSummaryResponseDTO(
                replyLetter.getId(),
                replyLetter.getTitle(),
                replyLetter.getLabel(),
                replyLetter.getCreatedAt()
        );
    }
}
