package online.bottler.complaint.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.infra.entity.MapComplaintEntity;

public interface MapComplaintJpaRepository extends JpaRepository<MapComplaintEntity, Long> {
    List<MapComplaintEntity> findByLetterId(Long letterId);
}
