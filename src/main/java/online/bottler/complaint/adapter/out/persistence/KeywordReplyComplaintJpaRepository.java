package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordReplyComplaintEntity;

public interface KeywordReplyComplaintJpaRepository extends JpaRepository<KeywordReplyComplaintEntity, Long> {
    List<KeywordReplyComplaintEntity> findByLetterId(Long letterId);
}
