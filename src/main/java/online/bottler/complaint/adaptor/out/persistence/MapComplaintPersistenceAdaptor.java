package online.bottler.complaint.adaptor.out.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.complaint.application.port.MapComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import online.bottler.complaint.adaptor.out.persistence.entity.MapComplaintEntity;

@Repository
@RequiredArgsConstructor
public class MapComplaintPersistenceAdaptor implements MapComplaintPersistencePort {
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
}
