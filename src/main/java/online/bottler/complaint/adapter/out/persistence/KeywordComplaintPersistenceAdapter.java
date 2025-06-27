package online.bottler.complaint.adapter.out.persistence;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bottler.complaint.adapter.out.persistence.entity.KeywordComplaintEntity;
import online.bottler.complaint.application.port.KeywordComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordComplaintPersistenceAdapter implements KeywordComplaintPersistencePort {
    private final KeywordComplaintJpaRepository repository;

    @Override
    public Complaint save(Complaint complaint) {
        return repository.save(KeywordComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        return Complaints.from(repository.findByLetterId(letterId)
                .stream().map(KeywordComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Complaint> findByLetterIdAndReporterId(Long letterId, Long reporterId) {
        return repository.findByLetterIdAndReporterId(letterId, reporterId)
                .map(KeywordComplaintEntity::toDomain);
    }
}
