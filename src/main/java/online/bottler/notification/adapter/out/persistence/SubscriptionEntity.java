package online.bottler.notification.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.notification.domain.Subscription;

@Entity
@Table(name = "subscription")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Embedded
    private EmbeddedDevice device;

    public static SubscriptionEntity from(Subscription subscription) {
        return SubscriptionEntity.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .device(new EmbeddedDevice(subscription.getDevice()))
                .build();
    }

    public Subscription toDomain() {
        return Subscription.builder()
                .id(id)
                .userId(userId)
                .device(device.toDomain())
                .build();
    }
}
