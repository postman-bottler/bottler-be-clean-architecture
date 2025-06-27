package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import online.bottler.complaint.adapter.out.persistence.entity.MapComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapComplaintJpaRepository extends JpaRepository<MapComplaintEntity, Long> {
    List<MapComplaintEntity> findByLetterId(Long letterId);

    Optional<MapComplaintEntity> findByLetterIdAndReporterId(Long letterId, Long reporterId);
}
