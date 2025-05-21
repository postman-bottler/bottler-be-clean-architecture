package online.bottler.complaint.infra;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.complaint.application.repository.KeywordReplyComplaintRepository;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import online.bottler.complaint.infra.entity.KeywordReplyComplaintEntity;

@Repository
@RequiredArgsConstructor
public class KeywordReplyComplaintRepositoryImpl implements KeywordReplyComplaintRepository {
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
}
