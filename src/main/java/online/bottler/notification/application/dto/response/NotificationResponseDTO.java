package online.bottler.notification.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;
import online.bottler.notification.domain.Notifications;

public record NotificationResponseDTO(
        UUID id,
        NotificationType type,
        Long receiver,
        LocalDateTime createdAt,
        Long letterId,
        Boolean isRead,
        String label
) {
    public static NotificationResponseDTO from(final Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getType(),
                notification.getReceiver(),
                notification.getCreatedAt(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification.getIsRead(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLabel() : null);
    }

    public static List<NotificationResponseDTO> from(final Notifications notifications) {
        return notifications.getNotifications().stream()
                .map(NotificationResponseDTO::from)
                .toList();
    }
}
