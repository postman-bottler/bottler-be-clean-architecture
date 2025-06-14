package online.bottler.notification.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    void deleteByDevice(EmbeddedDevice device);

    Boolean existsByDevice(EmbeddedDevice device);
}
