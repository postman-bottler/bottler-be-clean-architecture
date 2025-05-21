package online.bottler.notification.application.dto.response;

import online.bottler.notification.domain.Subscription;

public record SubscriptionResponseDTO(
        long userId
) {
    public static SubscriptionResponseDTO from(Subscription subscription) {
        return new SubscriptionResponseDTO(subscription.getUserId());
    }
}
