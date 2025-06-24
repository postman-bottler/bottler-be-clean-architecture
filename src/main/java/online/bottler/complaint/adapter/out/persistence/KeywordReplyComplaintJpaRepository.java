package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordReplyComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordReplyComplaintJpaRepository extends JpaRepository<KeywordReplyComplaintEntity, Long> {
    List<KeywordReplyComplaintEntity> findByLetterId(Long letterId);

    Optional<KeywordReplyComplaintEntity> findByLetterIdAndReporterId(Long letterId, Long reporterId);
}
