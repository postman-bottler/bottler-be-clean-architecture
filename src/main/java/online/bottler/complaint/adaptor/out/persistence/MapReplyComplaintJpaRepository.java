package online.bottler.complaint.adaptor.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adaptor.out.persistence.entity.MapReplyComplaintEntity;

public interface MapReplyComplaintJpaRepository extends JpaRepository<MapReplyComplaintEntity, Long> {
    List<MapReplyComplaintEntity> findByLetterId(Long letterId);
}
