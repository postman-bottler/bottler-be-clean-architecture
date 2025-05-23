package online.bottler.notification.application;

import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.ApplicationException;
import online.bottler.notification.application.dto.response.SubscriptionResponse;
import online.bottler.notification.application.port.SubscriptionUseCase;
import online.bottler.notification.application.port.SubscriptionPersistencePort;
import online.bottler.notification.domain.Subscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements SubscriptionUseCase {
    private final SubscriptionPersistencePort subscriptionPersistencePort;

    @Transactional
    public SubscriptionResponse subscribe(Long userId, String token) {
        Subscription subscribe = Subscription.create(userId, token);
        if (subscriptionPersistencePort.isDuplicate(subscribe)) {
            throw new ApplicationException("해당 기기는 이미 알림이 허용되어 있습니다.");
        }
        Subscription save = subscriptionPersistencePort.save(subscribe);
        return SubscriptionResponse.from(save);
    }

    @Transactional
    public void unsubscribeAll(Long userId) {
        subscriptionPersistencePort.deleteAllByUserId(userId);
    }

    @Transactional
    public void unsubscribe(String token) {
        subscriptionPersistencePort.deleteByToken(token);
    }
}
