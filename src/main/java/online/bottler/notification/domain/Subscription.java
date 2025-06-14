package online.bottler.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Subscription {
    private Long id;

    private Long userId;

    private Device device;

    public static Subscription create(Long userId, String token) {
        return Subscription.builder()
                .userId(userId)
                .device(new Device(token))
                .build();
    }

    public PushMessage makeMessage(NotificationType type) {
        return PushMessage.builder()
                .device(device)
                .title(type.getTitle())
                .content(type.getContent())
                .build();
    }
}
