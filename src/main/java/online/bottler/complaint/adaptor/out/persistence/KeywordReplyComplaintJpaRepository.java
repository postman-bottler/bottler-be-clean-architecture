package online.bottler.complaint.adaptor.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.complaint.adaptor.out.persistence.entity.KeywordReplyComplaintEntity;

public interface KeywordReplyComplaintJpaRepository extends JpaRepository<KeywordReplyComplaintEntity, Long> {
    List<KeywordReplyComplaintEntity> findByLetterId(Long letterId);
}
