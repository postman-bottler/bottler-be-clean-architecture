package online.bottler.notification.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.notification.application.port.NotificationPersistencePort;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.Notifications;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPersistencePort {

    private final NotificationJpaRepository repository;

    @Override
    public Notification save(Notification notification) {
        return repository.save(NotificationEntity.from(notification))
                .toDomain();
    }

    @Override
    public Notifications findByReceiver(Long userId) {
        return Notifications.from(repository.findByReceiverId(userId).stream()
                .map(NotificationEntity::toDomain)
                .toList());
    }

    @Override
    public void updateNotifications(Notifications notifications) {
        repository.saveAll(notifications.getNotifications().stream()
                .map(NotificationEntity::from)
                .toList());
    }
}
