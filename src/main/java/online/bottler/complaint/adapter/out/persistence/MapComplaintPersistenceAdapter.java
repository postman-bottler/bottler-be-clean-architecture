package online.bottler.complaint.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bottler.complaint.adapter.out.persistence.entity.MapComplaintEntity;
import online.bottler.complaint.application.port.MapComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MapComplaintPersistenceAdapter implements MapComplaintPersistencePort {
    private final MapComplaintJpaRepository jpaRepository;

    @Override
    public Complaint save(Complaint complaint) {
        return jpaRepository.save(MapComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<MapComplaintEntity> entities = jpaRepository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(MapComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Complaint> findByLetterIdAndReporterId(Long letterId, Long reporterId) {
        return jpaRepository.findByLetterIdAndReporterId(letterId, reporterId)
                .map(MapComplaintEntity::toDomain);
    }
}
