package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordComplaintJpaRepository extends JpaRepository<KeywordComplaintEntity, Long> {
    List<KeywordComplaintEntity> findByLetterId(Long letterId);

    Optional<KeywordComplaintEntity> findByLetterIdAndReporterId(Long letterId, Long reporterId);
}
