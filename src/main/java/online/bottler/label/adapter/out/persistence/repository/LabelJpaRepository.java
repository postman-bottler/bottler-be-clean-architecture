package online.bottler.label.adapter.out.persistence.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import online.bottler.label.adapter.out.persistence.entity.LabelEntity;
import online.bottler.label.domain.LabelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabelJpaRepository extends JpaRepository<LabelEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LabelEntity l WHERE l.labelId = :labelId")
    Optional<LabelEntity> findByIdWithLock(@Param("labelId") Long labelId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<LabelEntity> findByLabelType(LabelType labelType);
}
