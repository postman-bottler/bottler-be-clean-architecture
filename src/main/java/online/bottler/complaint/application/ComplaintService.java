package online.bottler.complaint.application;

import lombok.RequiredArgsConstructor;
import online.bottler.complaint.application.port.*;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.ComplaintType;
import online.bottler.complaint.domain.Complaints;
import online.bottler.letter.application.LetterService;
import online.bottler.letter.application.ReplyLetterService;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.MapLetterService;
import online.bottler.notification.application.NotificationService;
import online.bottler.notification.domain.NotificationType;
import online.bottler.user.application.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplaintService implements ComplaintUseCase {
    private final KeywordComplaintPersistencePort keywordComplaintPersistencePort;
    private final MapComplaintPersistencePort mapComplaintPersistencePort;
    private final KeywordReplyComplaintPersistencePort keywordReplyComplaintPersistencePort;
    private final MapReplyComplaintPersistencePort mapReplyComplaintPersistencePort;

    private final NotificationService notificationService;
    private final LetterService letterService;
    private final ReplyLetterService replyLetterService;
    private final MapLetterService mapLetterService;
    private final UserService userService;

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
        notificationService.sendNotification(NotificationType.WARNING, writerId, letterId, null);
        userService.updateWarningCount(writerId);
    }

    private Long blockLetter(ComplaintType type, Long letterId) {
        return switch (type) {
            case MAP_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            case MAP_REPLY_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.REPLY, letterId);
            case KEYWORD_LETTER -> letterService.softBlockLetter(letterId);
            case KEYWORD_REPLY_LETTER -> replyLetterService.softBlockLetter(letterId);
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
