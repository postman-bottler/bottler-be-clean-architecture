package online.bottler.notification.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Notification {
    private Long id;

    private final NotificationType type;

    private final long receiverId;

    private final LocalDateTime createdAt;

    private final Boolean isRead;

    protected Notification(NotificationType type, Long receiverId, Boolean isRead) {
        this.type = type;
        this.receiverId = receiverId;
        this.createdAt = LocalDateTime.now();
        this.isRead = isRead;
    }

    protected Notification(Long id, NotificationType type, Long receiverId, LocalDateTime createdAt, Boolean isRead) {
        this.id = id;
        this.type = type;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static Notification create(NotificationType type, Long receiver, Long letterId, String label) {
        if (type.isLetterNotification()) {
            return new LetterNotification(type, receiver, letterId, false, label);
        }
        return new Notification(type, receiver, false);
    }

    public static Notification of(Long id, NotificationType type, Long receiver,
                                  Long letterId, LocalDateTime createdAt, Boolean isRead, String label) {
        if (type.isLetterNotification()) {
            return new LetterNotification(id, type, receiver, letterId, createdAt, isRead, label);
        }
        return new Notification(id, type, receiver, createdAt, isRead);
    }

    public Boolean isLetterNotification() {
        return type.isLetterNotification();
    }

    public Notification read() {
        return Notification.of(id, type, receiverId,
                isLetterNotification() ? ((LetterNotification) this).getLetterId() : null, createdAt, true,
                isLetterNotification() ? ((LetterNotification) this).getLabelUrl() : null);
    }
}
