package online.bottler.notification.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long receiverId;

    private LocalDateTime createdAt;

    private Boolean isRead;

    private Long letterId;

    private String labelUrl;

    public static NotificationEntity from(Notification notification) {
        return new NotificationEntity(
                notification.getId(),
                notification.getType(),
                notification.getReceiverId(),
                notification.getCreatedAt(),
                notification.getIsRead(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLabelUrl() : null
        );
    }

    public Notification toDomain() {
        return Notification.of(id, type, receiverId, letterId, createdAt, isRead, labelUrl);
    }
}
