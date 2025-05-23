package online.bottler.user.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.user.adapter.out.persistence.entity.BanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanJpaRepository extends JpaRepository<BanEntity, Long> {
    List<BanEntity> findByUnbansAtBefore(LocalDateTime now);
}
