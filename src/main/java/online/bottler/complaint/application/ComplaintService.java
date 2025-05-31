package online.bottler.complaint.application;

import lombok.RequiredArgsConstructor;
import online.bottler.complaint.application.port.*;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.ComplaintType;
import online.bottler.complaint.domain.Complaints;
import online.bottler.letter.application.port.in.BlockLetterUseCase;
import online.bottler.letter.application.port.in.BlockReplyLetterUseCase;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.notification.application.port.NotificationUseCase;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplaintService implements ComplaintUseCase {
    private final KeywordComplaintPersistencePort keywordComplaintPersistencePort;
    private final MapComplaintPersistencePort mapComplaintPersistencePort;
    private final KeywordReplyComplaintPersistencePort keywordReplyComplaintPersistencePort;
    private final MapReplyComplaintPersistencePort mapReplyComplaintPersistencePort;

    private final NotificationUseCase notificationUseCase;
    private final BlockLetterUseCase blockLetterUseCase;
    private final BlockReplyLetterUseCase blockReplyLetterUseCase;
    private final MapLetterUseCase mapLetterUseCase;
    private final UserUseCase userUseCase;

    @Override
    @Transactional
    public ComplaintResponse complain(ComplaintCommand complaintCommand) {
        Complaint newComplaint = complaintCommand.toComplaint();
        Complaints existingComplaints = findExistingComplaints(complaintCommand.type(), complaintCommand.letterId());
        existingComplaints.add(newComplaint);
        if (existingComplaints.needWarning()) {
            sendWarningToWriter(complaintCommand.type(), complaintCommand.letterId());
        }
        return ComplaintResponse.from(saveComplaint(newComplaint, complaintCommand.type()));
    }

    private Complaints findExistingComplaints(ComplaintType type, Long letterId) {
        ComplaintPersistencePort repository = getRepositoryByType(type);
        return repository.findByLetterId(letterId);
    }

    private void sendWarningToWriter(ComplaintType type, Long letterId) {
        Long writerId = blockLetter(type, letterId);
        notificationUseCase.sendWarningNotification(writerId);
        userUseCase.updateWarningCount(writerId);
    }

    private Long blockLetter(ComplaintType type, Long letterId) {
        return switch (type) {
            case MAP_LETTER -> mapLetterUseCase.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            case MAP_REPLY_LETTER -> mapLetterUseCase.letterBlock(BlockMapLetterType.REPLY, letterId);
            case KEYWORD_LETTER -> blockLetterUseCase.softBlock(letterId);
            case KEYWORD_REPLY_LETTER -> blockReplyLetterUseCase.softBlock(letterId);
        };
    }

    private Complaint saveComplaint(Complaint complaint, ComplaintType type) {
        ComplaintPersistencePort repository = getRepositoryByType(type);
        return repository.save(complaint);
    }

    private ComplaintPersistencePort getRepositoryByType(ComplaintType type) {
        return switch (type) {
            case MAP_LETTER -> mapComplaintPersistencePort;
            case MAP_REPLY_LETTER -> mapReplyComplaintPersistencePort;
            case KEYWORD_LETTER -> keywordComplaintPersistencePort;
            case KEYWORD_REPLY_LETTER -> keywordReplyComplaintPersistencePort;
        };
    }
}
