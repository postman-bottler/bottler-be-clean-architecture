package online.bottler.mapletter.application.response;

import lombok.Builder;
import java.time.LocalDateTime;
import online.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record OneReplyLetterResponse(
        String content,
        Long sourceLetterId,
        String font,
        String label,
        String paper,
        LocalDateTime createdAt,
        boolean isOwner
) {
    public static OneReplyLetterResponse from(ReplyMapLetter replyMapLetter, boolean isOwner) {
        return OneReplyLetterResponse.builder()
                .content(replyMapLetter.getContent())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .label(replyMapLetter.getLabel())
                .paper(replyMapLetter.getPaper())
                .createdAt(replyMapLetter.getCreatedAt())
                .isOwner(isOwner)
                .build();
    }
}
