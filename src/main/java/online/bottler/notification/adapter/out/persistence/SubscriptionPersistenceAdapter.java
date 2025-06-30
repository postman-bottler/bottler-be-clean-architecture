package online.bottler.notification.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.notification.application.port.SubscriptionPersistencePort;
import online.bottler.notification.domain.Device;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionPersistencePort {
    private final SubscriptionJpaRepository repository;
    private final CacheRepository cacheRepository;

    @Override
    public Subscription save(Subscription subscription) {
        cacheRepository.save(subscription);
        return repository.save(SubscriptionEntity.from(subscription))
                .toDomain();
    }

    @Override
    public Subscriptions findByUserId(Long userId) {
        Subscriptions subscriptions = cacheRepository.findByUserId(userId);
        if (subscriptions.isEmpty()) {
            subscriptions = Subscriptions.from(repository.findByUserId(userId).stream()
                    .map(SubscriptionEntity::toDomain)
                    .toList());
            cacheRepository.save(subscriptions);
        }
        return subscriptions;
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
        cacheRepository.deleteAllByUserId(userId);
        repository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteByDevice(Long userId, Device device) {
        cacheRepository.deleteByDevice(userId, device);
        repository.deleteByDevice(new EmbeddedDevice(device));
    }

    @Override
    public Boolean checkDeviceDuplicate(Device device) {
        return repository.existsByDevice(new EmbeddedDevice(device));
    }
}
