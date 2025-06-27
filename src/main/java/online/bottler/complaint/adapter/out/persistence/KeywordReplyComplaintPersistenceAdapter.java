package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordReplyComplaintEntity;
import online.bottler.complaint.application.port.KeywordReplyComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordReplyComplaintPersistenceAdapter implements KeywordReplyComplaintPersistencePort {
    private final KeywordReplyComplaintJpaRepository repository;

    @Override
    public Complaint save(Complaint complaint) {
        return repository.save(KeywordReplyComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<KeywordReplyComplaintEntity> entities = repository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(KeywordReplyComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Complaint> findByLetterIdAndReporterId(Long letterId, Long reporterId) {
        return repository.findByLetterIdAndReporterId(letterId, reporterId)
                .map(KeywordReplyComplaintEntity::toDomain);
    }
}
