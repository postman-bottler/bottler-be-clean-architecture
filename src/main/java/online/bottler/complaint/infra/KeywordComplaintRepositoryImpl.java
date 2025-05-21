package online.bottler.complaint.infra;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.complaint.application.repository.KeywordComplaintRepository;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import online.bottler.complaint.infra.entity.KeywordComplaintEntity;

@Repository
@RequiredArgsConstructor
public class KeywordComplaintRepositoryImpl implements KeywordComplaintRepository {
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
}
