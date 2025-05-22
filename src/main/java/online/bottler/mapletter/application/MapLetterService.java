package online.bottler.mapletter.application;

import static online.bottler.mapletter.application.DeleteLetterType.MAP;
import static online.bottler.mapletter.application.DeleteLetterType.REPLY;
import static online.bottler.notification.domain.NotificationType.MAP_REPLY;
import static online.bottler.notification.domain.NotificationType.TARGET_LETTER;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.ApplicationException;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand.LetterInfo;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;
import online.bottler.mapletter.application.dto.FindAllArchiveLettersDTO;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.port.out.ReplyMapLetterPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.response.CheckReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllArchiveLettersResponse;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllReceivedReplyLetterResponse;
import online.bottler.mapletter.application.response.FindAllReplyMapLettersResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.application.response.OneReplyLetterResponse;
import online.bottler.mapletter.application.port.out.MapLetterArchivePersistencePort;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterArchive;
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
    private final MapLetterArchivePersistencePort mapLetterArchivePersistencePort;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RecentReplyCachePort recentReplyCachePort;

    private static final double VIEW_DISTANCE = 15;

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
    @Transactional
    public OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);

        Double distance = mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(
                latitude, longitude, letterId);

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);
        mapLetter.validateAccess(userId);

        if (mapLetter.isTargetUser(userId)) {
            mapLetterPersistencePort.updateRead(mapLetter);
        }

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, mapLetter.getCreateUserId() == userId,
                checkReplyMapLetter(letterId, userId).isReplied(), isArchived(letterId, userId));
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
    public List<FindNearbyLettersResponse> findNearByMapLetters(
            BigDecimal latitude, BigDecimal longitude, Long userId) {
        List<MapLetterAndDistance> letters = mapLetterPersistencePort.findLettersByUserLocation(latitude, longitude,
                userId);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponse.from(letter, nickname);
                        }
                ).toList();
    }

    @Override
    @Transactional
    public ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterCommand createReplyMapLetterCommand, Long userId) {
        boolean isReplied = replyMapLetterPersistencePort.findByLetterIdAndUserId(
                createReplyMapLetterCommand.sourceLetter(), userId);
        if (isReplied) {
            throw new ApplicationException("해당 지도 편지에 이미 답장을 했습니다.");
        }

        MapLetter source = mapLetterPersistencePort.findSourceMapLetterById(createReplyMapLetterCommand.sourceLetter());
        ReplyMapLetter replyMapLetter = CreateReplyMapLetterCommand.toReplyMapLetter(
                createReplyMapLetterCommand, userId);
        ReplyMapLetter save = replyMapLetterPersistencePort.save(replyMapLetter);

        MapLetter sourceLetter = mapLetterPersistencePort.findById(save.getSourceLetterId());
        recentReplyCachePort.saveRecentReply(
                sourceLetter.getCreateUserId(), ReplyType.MAP.name(), save.getReplyLetterId(), save.getLabel());

        notificationService.sendLetterNotification(
                MAP_REPLY, source.getCreateUserId(), save.getReplyLetterId(), save.getLabel());

        return save;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindAllReplyMapLettersResponse> findAllReplyMapLetter(int page, int size, Long letterId, Long userId) {
        validMinPage(page);
        MapLetter sourceLetter = mapLetterPersistencePort.findById(letterId);

        sourceLetter.validFindAllReplyMapLetter(userId);

        Page<ReplyMapLetter> findReply = replyMapLetterPersistencePort.findReplyMapLettersBySourceLetterId(letterId,
                PageRequest.of(page - 1, size));

        if (findReply.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(findReply.getTotalPages(), page);

        return findReply.map(FindAllReplyMapLettersResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public OneReplyLetterResponse findOneReplyMapLetter(Long letterId, Long userId) {
        ReplyMapLetter replyMapLetter = replyMapLetterPersistencePort.findById(letterId);
        MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());

        replyMapLetter.validFindOneReplyMapLetter(userId, sourceLetter);
        sourceLetter.validDeleteAndBlocked();

        return OneReplyLetterResponse.from(replyMapLetter, userId == replyMapLetter.getCreateUserId());
    }

    @Override
    @Transactional
    public MapLetterArchive mapLetterArchive(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);
        mapLetter.validMapLetterArchive();

        MapLetterArchive archive = MapLetterArchive.builder().mapLetterId(letterId).userId(userId)
                .createdAt(LocalDateTime.now()).build();

        if (isArchived(letterId, userId)) {
            throw new ApplicationException("해당 지도 편지가 이미 저장되어 있습니다.");
        }

        return mapLetterArchivePersistencePort.save(archive);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindAllArchiveLettersResponse> findArchiveLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindAllArchiveLettersDTO> letters = mapLetterArchivePersistencePort.findAllById(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);
        return letters.map(FindAllArchiveLettersDTO::toFindAllArchiveLettersResponse);
    }

    @Override
    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersCommand deleteArchivedLettersCommand, Long userId) {
        List<MapLetterArchive> archiveInfos = mapLetterArchivePersistencePort.findAllById(
                deleteArchivedLettersCommand.letterIds());

        if (archiveInfos.size() != deleteArchivedLettersCommand.letterIds().size()) {
            throw new ApplicationException("삭제할 지도 편지를 찾을 수 없습니다.");
        } //일부 아이디가 존재하지 않을 경우 예외 처리

        for (MapLetterArchive archiveInfo : archiveInfos) {
            archiveInfo.validDeleteArchivedLetter(userId);
        }

        mapLetterArchivePersistencePort.deleteAllByIdInBatch(deleteArchivedLettersCommand.letterIds());
    }

    @Override
    @Transactional(readOnly = true)
    public CheckReplyMapLetterResponse checkReplyMapLetter(Long letterId, Long userId) {
        return new CheckReplyMapLetterResponse(replyMapLetterPersistencePort.findByLetterIdAndUserId(letterId, userId));
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
    @Transactional
    public void deleteReplyMapLetter(DeleteReplyMapLettersCommand deleteReplyMapLettersCommand, Long userId) {
        List<ReplyMapLetter> replyMapLetters = new ArrayList<>();

        for (Long letterId : deleteReplyMapLettersCommand.letterIds()) {
            ReplyMapLetter replyMapLetter = replyMapLetterPersistencePort.findById(letterId);
            replyMapLetter.validDeleteReplyMapLetter(userId);
            replyMapLetterPersistencePort.softDelete(letterId);

            replyMapLetters.add(replyMapLetter);
        }
        deleteRecentRepliesFromRedis(replyMapLetters);
    }

    private void deleteRecentRepliesFromRedis(List<ReplyMapLetter> replyMapLetters) {
        for (ReplyMapLetter replyMapLetter : replyMapLetters) {
            MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());
            recentReplyCachePort.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                    replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OneLetterResponse findArchiveOneLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);
        mapLetter.validateAccess(userId);

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, mapLetter.getCreateUserId() == userId,
                checkReplyMapLetter(letterId, userId).isReplied(), isArchived(letterId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindAllSentReplyMapLetterResponse> findAllSentReplyMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterPersistencePort.findAllSentReplyByUserId(userId,
                PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();

            return FindAllSentReplyMapLetterResponse.from(replyMapLetter, title, REPLY);
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
    public Page<FindAllReceivedReplyLetterResponse> findAllReceivedReplyLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterPersistencePort.findActiveReplyMapLettersBySourceUserId(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterPersistencePort.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();
            return FindAllReceivedReplyLetterResponse.from(replyMapLetter, title, MAP);
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

    private void validMaxPage(int maxPage, int nowPage) {
        if (maxPage < nowPage) {
            throw new ApplicationException("페이지가 존재하지 않습니다.");
        }
    }

    private void validMinPage(int nowPage) {
        if (nowPage < 1) {
            throw new ApplicationException("페이지가 존재하지 않습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        List<MapLetterAndDistance> letters = mapLetterPersistencePort.guestFindLettersByUserLocation(latitude,
                longitude);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponse.from(letter, nickname);
                        }
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);

        Double distance = mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);

        mapLetter.isPrivate();

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, false, false, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationLabelRequestDTO> getLabels(List<Long> ids) {
        List<MapLetter> finds = mapLetterPersistencePort.findAllByIds(ids);
        return finds.stream()
                .map(find -> new NotificationLabelRequestDTO(find.getId(), find.getLabel()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isArchived(Long letterId, Long userId) {
        return mapLetterArchivePersistencePort.findByLetterIdAndUserId(letterId, userId);
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
}
