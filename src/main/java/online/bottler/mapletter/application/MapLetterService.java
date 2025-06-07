package online.bottler.mapletter.application;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.reply.application.ReplyType;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapLetterService implements MapLetterUseCase {
    private final MapLetterPersistencePort mapLetterPersistencePort;
    private final ReplyMapLetterPersistencePort replyMapLetterPersistencePort;
    private final RecentReplyCachePort recentReplyCachePort;

    @Override
    @Transactional
    public MapLetter createPublicMapLetter(CreatePublicMapLetterCommand createPublicMapLetterCommand, Long userId) {
        MapLetter mapLetter = createPublicMapLetterCommand.toPublicMapLetter(userId);
        return mapLetterPersistencePort.save(mapLetter);
    }

    @Override
    @Transactional
    public MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand, Long userId,
                                           Long targetUserId) {
        MapLetter mapLetter = createTargetMapLetterCommand.toTargetMapLetter(userId, targetUserId);
        return mapLetterPersistencePort.save(mapLetter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindSentMapLetter> findSentLetters(int page, int size, Long userId) {
        return mapLetterPersistencePort.findSentLettersByUserId(userId,
                PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FindReceivedMapLetterDTO> findReceivedMapLetters(int page, int size, Long userId) {
        return mapLetterPersistencePort.findActiveReceivedMapLettersByUserId(userId,
                PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        List<MapLetter> mapLetters = mapLetterPersistencePort.findAllByIds(letters);
        mapLetters.forEach(letter -> {
            letter.validDeleteMapLetter(userId);
        });
        mapLetterPersistencePort.softDeleteAll(mapLetters);
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
    public MapLetter findById(Long id) {
        return mapLetterPersistencePort.findById(id);
    }

    @Override
    @Transactional
    public void targetUserUpdateRead(Long userId, MapLetter mapLetter) {
        if (mapLetter.isTargetUser(userId)) {
            mapLetterPersistencePort.updateRead(mapLetter);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MapLetter> findActiveByCreateUserId(int page, int size, Long userId) {
        return mapLetterPersistencePort.findActiveByCreateUserId(userId, PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MapLetter> findActiveLetter(int page, int size, Long userId) {
        return mapLetterPersistencePort.findActiveByTargetUserId(userId, PageRequest.of(page - 1, size));
    }
}
