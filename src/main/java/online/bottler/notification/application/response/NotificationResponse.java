package online.bottler.notification.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;
import online.bottler.notification.domain.Notifications;

public record NotificationResponse(
        Long id,
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
                notification.getReceiverId(),
                notification.getCreatedAt(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification.getIsRead(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLabelUrl() : null);
    }

    public static List<NotificationResponse> from(final Notifications notifications) {
        return notifications.getNotifications().stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
