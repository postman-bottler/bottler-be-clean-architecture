package online.bottler.mapletter.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllSentReplyMapLetterResponse(
        Long letterId,
        String title,
        Long sourceLetterId,
        LocalDateTime createdAt,
        String label,
        DeleteLetterType deleteType //삭제 타입
) {
    public static FindAllSentReplyMapLetterResponse from(ReplyMapLetter replyMapLetter, String title,
                                                         DeleteLetterType deleteType) {
        return FindAllSentReplyMapLetterResponse.builder()
                .letterId(replyMapLetter.getReplyLetterId())
                .title(title)
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .createdAt(replyMapLetter.getCreatedAt())
                .label(replyMapLetter.getLabel())
                .deleteType(deleteType)
                .build();
    }
}
