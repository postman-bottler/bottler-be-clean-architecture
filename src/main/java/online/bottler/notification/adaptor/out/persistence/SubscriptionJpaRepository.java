package online.bottler.notification.adaptor.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    void deleteByToken(String token);

    Boolean existsByUserIdAndToken(Long userId, String token);
}
