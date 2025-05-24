package online.bottler.notification.domain;

import lombok.Getter;
import online.bottler.global.exception.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class LetterNotification extends Notification {
    private final long letterId;

    private final String label;

    protected LetterNotification(NotificationType type, long receiver, Long letterId, Boolean isRead, String label) {
        super(type, receiver, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
        this.label = label;
    }

    protected LetterNotification(UUID id, NotificationType type, long receiver,
                                 Long letterId, LocalDateTime createdAt, Boolean isRead, String label) {
        super(id, type, receiver, createdAt, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
        this.label = label;
    }

    private void validateLetterId(Long letterId) {
        if (letterId == null) {
            throw new DomainException("편지 관련 알림은 편지 ID가 있어야 합니다.");
        }
    }
}
