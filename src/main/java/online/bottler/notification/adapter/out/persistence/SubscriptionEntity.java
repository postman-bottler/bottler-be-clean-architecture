package online.bottler.notification.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private String token;

    public static SubscriptionEntity from(Subscription subscription) {
        return SubscriptionEntity.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .token(subscription.getToken())
                .build();
    }

    public Subscription toDomain() {
        return Subscription.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .build();
    }
}
