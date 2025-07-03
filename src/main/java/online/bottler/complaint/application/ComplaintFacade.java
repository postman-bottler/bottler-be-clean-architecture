package online.bottler.complaint.application;

import lombok.RequiredArgsConstructor;
import online.bottler.complaint.application.port.ComplaintUseCase;
import online.bottler.complaint.domain.ComplaintType;
import online.bottler.letter.application.port.in.BlockLetterUseCase;
import online.bottler.letter.application.port.in.BlockReplyLetterUseCase;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.notification.application.port.NotificationUseCase;
import online.bottler.user.application.UserFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ComplaintFacade {
    private final ComplaintUseCase complaintUseCase;
    private final NotificationUseCase notificationUseCase;
    private final BlockLetterUseCase blockLetterUseCase;
    private final BlockReplyLetterUseCase blockReplyLetterUseCase;
    private final MapLetterUseCase mapLetterUseCase;
    private final UserFacade userFacade;

    @Transactional
    public ComplaintResponse complain(ComplaintCommand complaintCommand) {
        ComplaintResponse complaintResponse = complaintUseCase.complain(complaintCommand);
        if (complaintUseCase.needWarning(complaintCommand.type(), complaintCommand.letterId())) {
            sendWarningToWriter(complaintCommand.type(), complaintCommand.letterId());
        }
        return complaintResponse;
    }

    private void sendWarningToWriter(ComplaintType type, Long letterId) {
        Long writerId = blockLetter(type, letterId);
        notificationUseCase.sendWarningNotification(writerId);
        userFacade.updateWarningCount(writerId);
    }

    private Long blockLetter(ComplaintType type, Long letterId) {
        return switch (type) {
            case MAP_LETTER -> mapLetterUseCase.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            case MAP_REPLY_LETTER -> mapLetterUseCase.letterBlock(BlockMapLetterType.REPLY, letterId);
            case KEYWORD_LETTER -> blockLetterUseCase.softBlock(letterId);
            case KEYWORD_REPLY_LETTER -> blockReplyLetterUseCase.softBlock(letterId);
        };
    }

}
