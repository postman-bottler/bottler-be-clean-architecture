package online.bottler.mapletter.application;

import static online.bottler.notification.domain.NotificationType.MAP_REPLY;
import static online.bottler.notification.domain.NotificationType.TARGET_LETTER;

import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.port.in.MapLetterReplyUseCase;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.notification.application.port.NotificationUseCase;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapLetterCreateFacade {

    private final MapLetterUseCase mapLetterUseCase;
    private final UserUseCase userUseCase;
    private final MapLetterReplyUseCase mapLetterReplyUseCase;
    private final NotificationUseCase notificationUseCase;

    public MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand, Long userId) {
        Long targetUserId = userUseCase.getUserIdByNickname(createTargetMapLetterCommand.target());
        MapLetter save = mapLetterUseCase.createTargetMapLetter(createTargetMapLetterCommand, userId, targetUserId);
        notificationUseCase.sendLetterNotification(TARGET_LETTER, targetUserId, save.getId(), save.getLabel());
        return save;
    }

    public ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterCommand createReplyMapLetterCommand, Long userId) {
        MapLetter source = mapLetterReplyUseCase.findSourceMapLetter(createReplyMapLetterCommand.sourceLetter());
        ReplyMapLetter saveLetter = mapLetterReplyUseCase.createReplyMapLetter(
                createReplyMapLetterCommand, userId, source);

        notificationUseCase.sendLetterNotification(
                MAP_REPLY, source.getCreateUserId(), saveLetter.getReplyLetterId(), saveLetter.getLabel());

        return saveLetter;
    }
}
