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
    public static MapLetter toTargetMapLetter(CreateTargetMapLetterCommand command, Long userId, Long targetUserId) {
        return MapLetter.builder()
                .title(command.title)
                .content(command.content)
                .latitude(command.latitude)
                .longitude(command.longitude)
                .font(command.font)
                .paper(command.paper)
                .label(command.label)
                .type(MapLetterType.PRIVATE)
                .createUserId(userId)
                .targetUserId(targetUserId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .isBlocked(false)
                .isRead(false)
                .description(command.description)
                .isRecipientDeleted(false)
                .build();
    }
}
