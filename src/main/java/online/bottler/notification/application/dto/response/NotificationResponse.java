package online.bottler.notification.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;
import online.bottler.notification.domain.Notifications;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        Long receiver,
        LocalDateTime createdAt,
        Long letterId,
        Boolean isRead,
        String label
) {
    public static NotificationResponse from(final Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getReceiver(),
                notification.getCreatedAt(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification.getIsRead(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLabel() : null);
    }

    public static List<NotificationResponse> from(final Notifications notifications) {
        return notifications.getNotifications().stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
