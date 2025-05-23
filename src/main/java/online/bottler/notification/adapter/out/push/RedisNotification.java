package online.bottler.notification.adapter.out.push;

import static online.bottler.notification.adapter.out.push.NotificationHashKey.CREATED_AT;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.ID;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.IS_READ;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.LABEL;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.LETTER_ID;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.RECEIVER;
import static online.bottler.notification.adapter.out.push.NotificationHashKey.TYPE;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import online.bottler.notification.domain.LetterNotification;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.NotificationType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RedisNotification {
    private UUID id;

    private Long receiver;

    private NotificationType type;

    private Long letterId;

    private LocalDateTime createdAt;

    private Boolean isRead;

    private String label;

    public static RedisNotification from(Notification notification) {
        return RedisNotification.builder()
                .id(notification.getId())
                .receiver(notification.getReceiver())
                .type(notification.getType())
                .letterId(notification.isLetterNotification() ?
                        ((LetterNotification) notification).getLetterId() : null)
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .label(notification.isLetterNotification() ? ((LetterNotification) notification).getLabel() : null)
                .build();
    }

    public Notification toDomain() {
        return Notification.of(id, type, receiver, letterId, createdAt, isRead, label);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID.getKey(), id);
        map.put(RECEIVER.getKey(), receiver);
        map.put(TYPE.getKey(), type);
        map.put(LETTER_ID.getKey(), letterId);
        map.put(CREATED_AT.getKey(), createdAt);
        map.put(IS_READ.getKey(), isRead);
        map.put(LABEL.getKey(), label);
        return map;
    }

    public static RedisNotification create(RedisTemplate<String, Object> redisTemplate, String key) {
        return RedisNotification.builder()
                .id((UUID) redisTemplate.opsForHash().get(key, ID.getKey()))
                .type((NotificationType) redisTemplate.opsForHash().get(key, TYPE.getKey()))
                .receiver((Long) redisTemplate.opsForHash().get(key, RECEIVER.getKey()))
                .createdAt((LocalDateTime) redisTemplate.opsForHash().get(key, CREATED_AT.getKey()))
                .letterId((Long) redisTemplate.opsForHash().get(key, LETTER_ID.getKey()))
                .isRead((Boolean) redisTemplate.opsForHash().get(key, IS_READ.getKey()))
                .label((String) redisTemplate.opsForHash().get(key, LABEL.getKey()))
                .build();
    }

    public String createRedisKey() {
        return ":" + receiver + ":" + id;
    }
}
