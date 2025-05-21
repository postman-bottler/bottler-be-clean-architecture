package online.bottler.notification.application.repository;

import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);

    Subscriptions findByUserId(Long userId);

    Subscriptions findAll();

    void deleteAllByUserId(Long userId);

    void deleteByToken(String token);

    Boolean isDuplicate(Subscription subscription);
}
