package online.bottler.mapletter.application.response;

import lombok.Builder;
import java.time.LocalDateTime;
import online.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllReplyMapLettersResponse(
        Long replyLetterId,
        String label,
        LocalDateTime createdAt
) {
    public static FindAllReplyMapLettersResponse from(ReplyMapLetter replyMapLetter) {
        return FindAllReplyMapLettersResponse.builder()
                .replyLetterId(replyMapLetter.getReplyLetterId())
                .label(replyMapLetter.getLabel())
                .createdAt(replyMapLetter.getCreatedAt())
                .build();
    }
}
