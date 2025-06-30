package online.bottler.notification.application.port;

import online.bottler.notification.domain.Device;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;

public interface SubscriptionPersistencePort {
    Subscription save(Subscription subscription);

    Subscriptions findByUserId(Long userId);

    Subscriptions findAll();

    void deleteAllByUserId(Long userId);

    void deleteByDevice(Long userId, Device device);

    Boolean checkDeviceDuplicate(Device device);
}
