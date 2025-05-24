package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordComplaintEntity;

public interface KeywordComplaintJpaRepository extends JpaRepository<KeywordComplaintEntity, Long> {
    List<KeywordComplaintEntity> findByLetterId(Long letterId);
}
