package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adapter.out.persistence.entity.MapReplyComplaintEntity;

public interface MapReplyComplaintJpaRepository extends JpaRepository<MapReplyComplaintEntity, Long> {
    List<MapReplyComplaintEntity> findByLetterId(Long letterId);
}
