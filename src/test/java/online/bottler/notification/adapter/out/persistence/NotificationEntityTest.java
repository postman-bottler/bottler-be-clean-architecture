package online.bottler.notification.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationEntityTest {

    @DisplayName("편지 관련 알림을 JPA 엔티티로 변환한다.")
    @Test
    void fromLetterNotification() {
        // given
        Notification notification = LetterNotification.of(1L, NotificationType.NEW_LETTER, 1L, 1L, LocalDateTime.now(),
                false,
                "label");

        // when
        NotificationEntity notificationEntity = NotificationEntity.from(notification);

        // then
        assertThat(notificationEntity).extracting("id", "type", "receiverId", "letterId", "createdAt", "isRead",
                        "labelUrl")
                .containsExactly(notification.getId(), notification.getType(), notification.getReceiverId(),
                        ((LetterNotification) notification).getLetterId(), notification.getCreatedAt(),
                        notification.getIsRead(), ((LetterNotification) notification).getLabelUrl());
    }

    @DisplayName("알림을 JPA 엔티티로 변환한다.")
    @Test
    void fromNotification() {
        // given
        Notification notification = Notification.of(1L, NotificationType.WARNING, 1L, null, LocalDateTime.now(), false,
                null);

        // when
        NotificationEntity notificationEntity = NotificationEntity.from(notification);

        // then
        assertThat(notificationEntity).extracting("id", "type", "receiverId", "letterId", "createdAt", "isRead",
                        "labelUrl")
                .containsExactly(notification.getId(), notification.getType(), notification.getReceiverId(),
                        null, notification.getCreatedAt(), notification.getIsRead(), null);
    }

    @DisplayName("편지 알림 JPA Entity를 도메인 객체로 변환한다.")
    @Test
    void letterNotificationToDomain() {
        // given
        NotificationEntity notificationEntity = new NotificationEntity(1L, NotificationType.NEW_LETTER, 1L,
                LocalDateTime.now(), false, 1L, "label");

        // when
        Notification notification = notificationEntity.toDomain();

        // then
        assertThat(notification).isInstanceOf(LetterNotification.class);
        assertThat((LetterNotification) notification).extracting("id", "type", "receiverId", "letterId",
                        "createdAt", "isRead", "labelUrl")
                .containsExactly(notificationEntity.getId(), notificationEntity.getType(),
                        notificationEntity.getReceiverId(), notificationEntity.getLetterId(),
                        notificationEntity.getCreatedAt(), notificationEntity.getIsRead(),
                        notificationEntity.getLabelUrl());
    }

    @DisplayName("알림 JPA Entity를 도메인 객체로 변환한다.")
    @Test
    void notificationToDomain() {
        // given
        NotificationEntity notificationEntity = new NotificationEntity(1L, NotificationType.WARNING, 1L,
                LocalDateTime.now(), false, null, null);

        // when
        Notification notification = notificationEntity.toDomain();

        // then
        assertThat(notification).isNotInstanceOf(LetterNotification.class);
        assertThat(notification).extracting("id", "type", "receiverId", "createdAt", "isRead")
                .containsExactly(notificationEntity.getId(), notificationEntity.getType(),
                        notificationEntity.getReceiverId(), notificationEntity.getCreatedAt(),
                        notificationEntity.getIsRead());
    }
}