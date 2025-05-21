package online.bottler.complaint.application.repository;

import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;

public interface ComplaintRepository {
    Complaint save(Complaint complaint);

    Complaints findByLetterId(Long letterId);
}
