package online.bottler.user.infra;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.user.infra.entity.BanEntity;

public interface BanJpaRepository extends JpaRepository<BanEntity, Long> {
    List<BanEntity> findByUnbansAtBefore(LocalDateTime now);
}
