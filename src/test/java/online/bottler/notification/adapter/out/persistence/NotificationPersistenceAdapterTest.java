package online.bottler.notification.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;
import online.bottler.notification.domain.Notifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class NotificationPersistenceAdapterTest {

    @Autowired
    private NotificationPersistenceAdapter notificationPersistenceAdapter;
    @Autowired
    private NotificationJpaRepository repository;

    @Transactional
    @DisplayName("편지 알림을 저장한다.")
    @Test
    void saveLetterNotification() {
        // given
        Notification notification = Notification.create(NotificationType.NEW_LETTER, 1L, 1L, "label");

        // when
        Notification save = notificationPersistenceAdapter.save(notification);

        // then
        Optional<NotificationEntity> find = repository.findById(save.getId());
        assertThat(find).isPresent();
        assertThat(find.get()).extracting("id", "type", "receiverId", "letterId", "createdAt", "isRead", "labelUrl")
                .containsExactly(save.getId(), save.getType(), save.getReceiverId(),
                        ((LetterNotification) save).getLetterId(), save.getCreatedAt(), save.getIsRead(),
                        ((LetterNotification) save).getLabelUrl());
    }

    @Transactional
    @DisplayName("알림을 저장한다.")
    @Test
    void saveNotification() {
        // given
        Notification notification = Notification.create(NotificationType.WARNING, 1L, null, null);

        // when
        Notification save = notificationPersistenceAdapter.save(notification);

        // then
        Optional<NotificationEntity> find = repository.findById(save.getId());
        assertThat(find).isPresent();
        assertThat(find.get()).extracting("id", "type", "receiverId", "letterId", "createdAt", "isRead", "labelUrl")
                .containsExactly(save.getId(), save.getType(), save.getReceiverId(), null, save.getCreatedAt(),
                        save.getIsRead(), null);
    }

    @Transactional
    @DisplayName("사용자의 알림을 조회한다.")
    @Test
    void findByReceiver() {
        // given
        Long receiver = 1L;
        NotificationEntity notification1 = new NotificationEntity(null, NotificationType.NEW_LETTER, receiver,
                LocalDateTime.now(), false, 1L, "label");
        NotificationEntity notification2 = new NotificationEntity(null, NotificationType.WARNING, receiver,
                LocalDateTime.now(), false, null, null);
        repository.save(notification1);
        repository.save(notification2);

        // when
        Notifications notifications = notificationPersistenceAdapter.findByReceiver(1L);

        // then
        assertThat(notifications.getNotifications()).hasSize(2);
        assertThat(notifications.getNotifications()).extracting("receiverId")
                .containsOnly(receiver);
    }

    @Transactional
    @DisplayName("알림의 바뀐 정보를 업데이트한다.")
    @Test
    void updateNotifications() {
        // given
        NotificationEntity save = repository.save(new NotificationEntity(null, NotificationType.NEW_LETTER, 1L,
                LocalDateTime.now(), false, 1L, "label"));
        Notifications notifications = notificationPersistenceAdapter.findByReceiver(1L);

        // when
        Notifications readNotifications = notifications.markAsRead();
        notificationPersistenceAdapter.updateNotifications(readNotifications);

        // then
        Optional<NotificationEntity> find = repository.findById(save.getId());
        assertThat(find).isPresent();
        assertThat(find.get().getIsRead()).isTrue();
    }
}