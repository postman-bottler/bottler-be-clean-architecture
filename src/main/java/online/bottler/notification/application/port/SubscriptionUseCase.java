package online.bottler.notification.application.port;

import online.bottler.notification.application.dto.response.SubscriptionResponse;

public interface SubscriptionUseCase {

    SubscriptionResponse subscribe(Long userId, String token);

    void unsubscribeAll(Long userId);

    void unsubscribe(String token);

}
