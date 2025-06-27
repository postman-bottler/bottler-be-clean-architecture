package online.bottler.complaint.application.port;

import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;

import java.util.Optional;

public interface ComplaintPersistencePort {
    Complaint save(Complaint complaint);

    Complaints findByLetterId(Long letterId);

    Optional<Complaint> findByLetterIdAndReporterId(Long letterId, Long reporterId);
}
