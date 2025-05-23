package online.bottler.mapletter.application.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;

public record CreateTargetMapLetterCommand(
        String title,
        String content,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String font,
        String paper,
        String label,
        String target
) {
    public MapLetter toTargetMapLetter(Long userId, Long targetUserId) {
        return MapLetter.builder()
                .title(title)
                .content(content)
                .latitude(latitude)
                .longitude(longitude)
                .font(font)
                .paper(paper)
                .label(label)
                .type(MapLetterType.PRIVATE)
                .createUserId(userId)
                .targetUserId(targetUserId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .isBlocked(false)
                .isRead(false)
                .description(description)
                .isRecipientDeleted(false)
                .build();
    }
}
