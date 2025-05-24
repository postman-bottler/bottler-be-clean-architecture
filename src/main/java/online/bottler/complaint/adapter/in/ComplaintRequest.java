package online.bottler.complaint.adapter.in;

import jakarta.validation.constraints.NotBlank;
import online.bottler.complaint.application.ComplaintCommand;
import online.bottler.complaint.domain.ComplaintType;

public record ComplaintRequest(
        @NotBlank(message = "신고 사유를 작성해주세요.")
        String description
) {
        public ComplaintCommand toCommand(ComplaintType complaintType, Long letterId, Long reporterId) {
                return new ComplaintCommand(
                        complaintType,
                        letterId,
                        reporterId,
                        description
                );
        }
}
