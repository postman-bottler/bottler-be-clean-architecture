package online.bottler.complaint.application.port;

import online.bottler.complaint.application.ComplaintCommand;
import online.bottler.complaint.application.ComplaintResponse;

public interface ComplaintUseCase {

    ComplaintResponse complain(ComplaintCommand complaintCommand);

}
