package online.bottler.complaint.application;

import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.ComplaintType;

public record ComplaintCommand(
        ComplaintType type,
        Long letterId,
        Long reporterId,
        String description
) {
    public Complaint toComplaint() {
        return Complaint.create(letterId, reporterId, description);
    }
    // TODO: ComplainedLetter - ComplaintType, Long letterId 개념 객체 추가
}
