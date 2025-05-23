package online.bottler.notification.adaptor.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.notification.application.port.SubscriptionPersistencePort;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionPersistenceAdaptor implements SubscriptionPersistencePort {
    private final SubscriptionJpaRepository repository;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity save = repository.save(SubscriptionEntity.from(subscription));
        return save.toDomain();
    }

    @Override
    public Subscriptions findByUserId(Long userId) {
        List<Subscription> subscriptions = repository.findByUserId(userId).stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
        return Subscriptions.from(subscriptions);
    }

    @Override
    public Subscriptions findAll() {
        List<Subscription> subscriptions = repository.findAll().stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
        return Subscriptions.from(subscriptions);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        repository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }

    @Override
    public Boolean isDuplicate(Subscription subscription) {
        return repository.existsByUserIdAndToken(subscription.getUserId(), subscription.getToken());
    }
}
