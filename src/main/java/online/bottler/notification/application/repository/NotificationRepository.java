package online.bottler.notification.application.repository;

import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.Notifications;

public interface NotificationRepository {
    Notification save(Notification notification);

    Notifications findByReceiver(Long userId);

    void updateNotifications(Notifications notifications);
}
