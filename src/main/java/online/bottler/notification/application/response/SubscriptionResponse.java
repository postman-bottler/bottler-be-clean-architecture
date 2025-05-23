package online.bottler.notification.application.response;

import online.bottler.notification.domain.Subscription;

public record SubscriptionResponse(
        long userId
) {
    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(subscription.getUserId());
    }
}
