package online.bottler.complaint.application;

import online.bottler.complaint.domain.Complaint;

public record ComplaintResponse(
        Long id,
        String description
) {
    public static ComplaintResponse from(Complaint complaint) {
        return new ComplaintResponse(complaint.getId(), complaint.getDescription());
    }
}
