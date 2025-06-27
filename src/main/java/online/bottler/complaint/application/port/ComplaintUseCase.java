package online.bottler.complaint.application.port;

import online.bottler.complaint.application.ComplaintCommand;
import online.bottler.complaint.application.ComplaintResponse;
import online.bottler.complaint.domain.ComplaintType;

public interface ComplaintUseCase {

    ComplaintResponse complain(ComplaintCommand complaintCommand);

    boolean needWarning(ComplaintType complaintType, Long letterId);
}
