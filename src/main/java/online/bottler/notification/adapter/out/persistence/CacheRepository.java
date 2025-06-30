package online.bottler.notification.adapter.out.persistence;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.notification.domain.Device;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheRepository {
    private final RedisTemplate<String, String> subscriptionTemplate;
    private final String REDIS_KEY_FORMAT = "subscription:%d";

    public void save(Subscription subscription) {
        String key = getKey(subscription.getUserId());
        subscriptionTemplate.opsForList().rightPush(key, subscription.getDevice().getToken());
        updateTtl(key);
    }

    public void save(Subscriptions subscriptions) {
        if (subscriptions.isEmpty()) {
            return;
        }
        for (Subscription subscription : subscriptions.getSubscriptions()) {
            save(subscription);
        }
    }

    public Subscriptions findByUserId(Long userId) {
        List<Subscription> subscriptions = new ArrayList<>();
        String key = getKey(userId);
        List<String> tokens = subscriptionTemplate.opsForList().range(key, 0, -1);
        if (tokens != null) {
            for (String token : tokens) {
                subscriptions.add(Subscription.create(userId, token));
            }
            updateTtl(key);
        }
        return Subscriptions.from(subscriptions);
    }

    public void deleteByDevice(Long userId, Device device) {
        String key = getKey(userId);
        subscriptionTemplate.opsForList().remove(key, 0, device.getToken());
    }

    public void deleteAllByUserId(Long userId) {
        String key = getKey(userId);
        subscriptionTemplate.delete(key);
    }

    private String getKey(Long userId) {
        return String.format(REDIS_KEY_FORMAT, userId);
    }

    private void updateTtl(String key) {
        subscriptionTemplate.expire(key, Duration.ofHours(6));
    }
}
