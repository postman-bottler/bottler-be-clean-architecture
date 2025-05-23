package online.bottler.complaint.adaptor.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adaptor.out.persistence.entity.MapComplaintEntity;

public interface MapComplaintJpaRepository extends JpaRepository<MapComplaintEntity, Long> {
    List<MapComplaintEntity> findByLetterId(Long letterId);
}
