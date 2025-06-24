package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import online.bottler.complaint.adapter.out.persistence.entity.MapReplyComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapReplyComplaintJpaRepository extends JpaRepository<MapReplyComplaintEntity, Long> {
    List<MapReplyComplaintEntity> findByLetterId(Long letterId);

    Optional<MapReplyComplaintEntity> findByLetterIdAndReporterId(Long letterId, Long reporterId);
}
