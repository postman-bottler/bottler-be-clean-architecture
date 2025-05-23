package online.bottler.notification.adapter.out.push;

import static online.bottler.notification.adapter.out.push.NotificationHashKey.NOTIFICATION_KEY;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.notification.application.port.NotificationPersistencePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import online.bottler.notification.domain.Notification;
import online.bottler.notification.domain.Notifications;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisPersistenceAdapter implements NotificationPersistencePort {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long TTL = 7;

    @Override
    public Notification save(Notification notification) {
        RedisNotification redisNotification = RedisNotification.from(notification);
        String notificationKey = NOTIFICATION_KEY.getKey() + redisNotification.createRedisKey();
        redisTemplate.opsForHash().putAll(notificationKey, redisNotification.toMap());
        redisTemplate.expire(notificationKey, TTL, TimeUnit.DAYS);
        return redisNotification.toDomain();
    }

    @Override
    public Notifications findByReceiver(Long userId) {
        Set<String> keys = redisTemplate.keys(NOTIFICATION_KEY.getKey() + ":" + userId + ":*");
        List<Notification> notifications = keys.stream()
                .map(key -> RedisNotification.create(redisTemplate, key))
                .map(RedisNotification::toDomain)
                .collect(Collectors.toList());
        return Notifications.from(notifications);
    }

    @Override
    public void updateNotifications(Notifications notifications) {
        notifications.getNotifications().forEach(notification -> {
            RedisNotification redisNotification = RedisNotification.from(notification);
            String notificationKey = NOTIFICATION_KEY.getKey() + redisNotification.createRedisKey();
            redisTemplate.opsForHash().putAll(notificationKey, redisNotification.toMap());
        });
    }
}
