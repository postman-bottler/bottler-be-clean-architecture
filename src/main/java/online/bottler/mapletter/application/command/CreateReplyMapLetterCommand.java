package online.bottler.mapletter.application.command;

import java.time.LocalDateTime;
import online.bottler.mapletter.domain.ReplyMapLetter;

public record CreateReplyMapLetterCommand(
        Long sourceLetter,
        String content,
        String font,
        String paper,
        String label
) {
    public ReplyMapLetter toReplyMapLetter(Long userId) {
        return ReplyMapLetter.builder()
                .sourceLetterId(sourceLetter)
                .font(font)
                .content(content)
                .paper(paper)
                .label(label)
                .isBlocked(false)
                .isDeleted(false)
                .createUserId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isRecipientDeleted(false)
                .build();
    }
}
