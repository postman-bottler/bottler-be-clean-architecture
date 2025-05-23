package online.bottler.mapletter.application;

import static online.bottler.mapletter.application.DeleteLetterType.MAP;
import static online.bottler.mapletter.application.DeleteLetterType.REPLY;
import static online.bottler.notification.domain.NotificationType.TARGET_LETTER;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.ApplicationException;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand.LetterInfo;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.port.out.ReplyMapLetterPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.notification.application.dto.request.NotificationLabelRequestDTO;
import online.bottler.notification.application.NotificationService;
import online.bottler.reply.application.ReplyType;
import online.bottler.user.application.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapLetterService implements MapLetterUseCase {
    private final MapLetterPersistencePort mapLetterPersistencePort;
    private final ReplyMapLetterPersistencePort replyMapLetterPersistencePort;
    private final RecentReplyCachePort recentReplyCachePort;
    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public MapLetter createPublicMapLetter(CreatePublicMapLetterCommand createPublicMapLetterCommand, Long userId) {
        MapLetter mapLetter = CreatePublicMapLetterCommand.toPublicMapLetter(createPublicMapLetterCommand, userId);
        return mapLetterPersistencePort.save(mapLetter);
    }

    @Override
    @Transactional
    public MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand, Long userId) {
        Long targetUserId = userService.getUserIdByNickname(createTargetMapLetterCommand.target());
        MapLetter mapLetter = CreateTargetMapLetterCommand.toTargetMapLetter(
                createTargetMapLetterCommand, userId, targetUserId);

        MapLetter save = mapLetterPersistencePort.save(mapLetter);
        notificationService.sendLetterNotification(TARGET_LETTER, targetUserId, save.getId(), save.getLabel());
        return save;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindMapLetterResponse> findSentMapLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindSentMapLetter> sentMapLetters = mapLetterPersistencePort.findSentLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        if (sentMapLetters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(sentMapLetters.getTotalPages(), page);

        return sentMapLetters.map(this::toFindSentMapLetter);
    }

    private FindMapLetterResponse toFindSentMapLetter(FindSentMapLetter findSentMapLetter) {
        String targetUserNickname = null;
        if (findSentMapLetter.getType().equals("TARGET")) {
            targetUserNickname = userService.getNicknameById(findSentMapLetter.getTargetUser());
        }

        return FindMapLetterResponse.from(findSentMapLetter, targetUserNickname,
                findSentMapLetter.getType().equals("REPLY") ? REPLY : MAP);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindReceivedMapLetterResponse> findReceivedMapLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindReceivedMapLetterDTO> letters = mapLetterPersistencePort.findActiveReceivedMapLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);

        return letters.map(letter -> {
            String senderNickname = null;
            String senderProfileImg = null;

            if ("TARGET".equals(letter.getType())) {
                senderNickname = userService.getNicknameById(letter.getSenderId());
                senderProfileImg = userService.getProfileImageUrlById(letter.getSenderId());
            }

            return FindReceivedMapLetterResponse.from(letter, senderNickname, senderProfileImg, MAP);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindAllSentMapLetterResponse> findAllSentMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterPersistencePort.findActiveByCreateUserId(userId,
                PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(mapLetter -> {
            String targetUserNickname = null;
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
                targetUserNickname = userService.getNicknameById(mapLetter.getTargetUserId());
            }
            return FindAllSentMapLetterResponse.from(mapLetter, targetUserNickname, MAP);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindAllReceivedLetterResponse> findAllReceivedLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterPersistencePort.findActiveByTargetUserId(userId,
                PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);
        return letters.map(letter -> {
            String sendUserNickname = userService.getNicknameById(letter.getCreateUserId());
            String sendUserProfileImg = userService.getProfileImageUrlById(letter.getCreateUserId());
            return FindAllReceivedLetterResponse.from(letter, sendUserNickname, sendUserProfileImg,
                    MAP);
        });
    }

    @Override
    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        for (Long letterId : letters) {
            MapLetter findMapLetter = mapLetterPersistencePort.findById(letterId);
            findMapLetter.validDeleteMapLetter(userId);
            mapLetterPersistencePort.softDelete(letterId);
        }
    }

    @Override
    @Transactional
    public void deleteAllMapLetters(String type, Long userId) {
        switch (type) {
            case "SENT":
                mapLetterPersistencePort.softDeleteAllByCreateUserId(userId);
                replyMapLetterPersistencePort.softDeleteAllByCreateUserId(userId);
                break;
            case "SENT-MAP":
                mapLetterPersistencePort.softDeleteAllByCreateUserId(userId);
                break;
            case "SENT-REPLY":
                replyMapLetterPersistencePort.softDeleteAllByCreateUserId(userId);
                break;
            case "RECEIVED":
                mapLetterPersistencePort.softDeleteAllForRecipient(userId);
                replyMapLetterPersistencePort.softDeleteAllForRecipient(userId);
                break;
            case "RECEIVED-MAP":
                mapLetterPersistencePort.softDeleteAllForRecipient(userId);
                break;
            case "RECEIVED-REPLY":
                replyMapLetterPersistencePort.softDeleteAllForRecipient(userId);
                break;
            default:
                throw new ApplicationException("잘못된 지도 편지 삭제 타입입니다.");
        }
    }

    @Override
    @Transactional
    public void deleteSentMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId) {
        for (LetterInfo letter : deleteMapLettersCommand.letters()) {
            switch (letter.letterType()) {
                case MAP:
                    MapLetter findMapLetter = mapLetterPersistencePort.findById(letter.letterId());
                    findMapLetter.validDeleteMapLetter(userId);
                    mapLetterPersistencePort.softDelete(letter.letterId());
                    break;
                case REPLY:
                    ReplyMapLetter replyMapLetter = replyMapLetterPersistencePort.findById(letter.letterId());
                    replyMapLetter.validDeleteReplyMapLetter(userId);
                    replyMapLetterPersistencePort.softDelete(letter.letterId());

                    MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());
                    recentReplyCachePort.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                            replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
                    break;
                default:
                    throw new ApplicationException("잘못된 보낸 지도 편지 삭제 타입입니다.");
            }
        }
    }

    @Override
    @Transactional
    public void deleteReceivedMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId) {
        for (LetterInfo letter : deleteMapLettersCommand.letters()) {
            switch (letter.letterType()) {
                case MAP:
                    MapLetter findMapLetter = mapLetterPersistencePort.findById(letter.letterId());
                    findMapLetter.validateRecipientDeletion(userId);
                    mapLetterPersistencePort.softDeleteForRecipient(letter.letterId());
                    break;
                case REPLY:
                    ReplyMapLetter replyMapLetter = replyMapLetterPersistencePort.findById(letter.letterId());
                    MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());
                    replyMapLetter.validateRecipientDeletion(userId, sourceLetter.getCreateUserId());
                    replyMapLetterPersistencePort.softDeleteForRecipient(letter.letterId());

                    recentReplyCachePort.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                            replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
                    break;
                default:
                    throw new ApplicationException("잘못된 받은 지도 편지 삭제 타입입니다.");
            }
        }
    }

    @Override
    @Transactional
    public Long letterBlock(BlockMapLetterType type, Long letterId) { //userId return
        if (type == BlockMapLetterType.MAP_LETTER) {
            mapLetterPersistencePort.letterBlock(letterId);
            return mapLetterPersistencePort.findById(letterId).getCreateUserId();
        } else if (type == BlockMapLetterType.REPLY) {
            replyMapLetterPersistencePort.letterBlock(letterId);
            return replyMapLetterPersistencePort.findById(letterId).getCreateUserId();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationLabelRequestDTO> getLabels(List<Long> ids) {
        List<MapLetter> finds = mapLetterPersistencePort.findAllByIds(ids);
        return finds.stream()
                .map(find -> new NotificationLabelRequestDTO(find.getId(), find.getLabel()))
                .toList();
    }

    void validMaxPage(int maxPage, int nowPage) {
        if (maxPage < nowPage) {
            throw new ApplicationException("페이지가 존재하지 않습니다.");
        }
    }

    void validMinPage(int nowPage) {
        if (nowPage < 1) {
            throw new ApplicationException("페이지가 존재하지 않습니다.");
        }
    }
}
