package online.bottler.notification.application.port;

import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.Notifications;

public interface NotificationPersistencePort {
    Notification save(Notification notification);

    Notifications findByReceiver(Long userId);

    void updateNotifications(Notifications notifications);
}
