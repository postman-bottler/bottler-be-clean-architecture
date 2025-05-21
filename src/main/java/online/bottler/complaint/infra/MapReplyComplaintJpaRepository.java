package online.bottler.complaint.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.infra.entity.MapReplyComplaintEntity;

public interface MapReplyComplaintJpaRepository extends JpaRepository<MapReplyComplaintEntity, Long> {
    List<MapReplyComplaintEntity> findByLetterId(Long letterId);
}
