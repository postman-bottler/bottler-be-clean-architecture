package online.bottler.complaint.adaptor.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adaptor.out.persistence.entity.KeywordComplaintEntity;

public interface KeywordComplaintJpaRepository extends JpaRepository<KeywordComplaintEntity, Long> {
    List<KeywordComplaintEntity> findByLetterId(Long letterId);
}
