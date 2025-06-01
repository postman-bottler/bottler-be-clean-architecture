package online.bottler.letter.application;

import static online.bottler.notification.domain.NotificationType.KEYWORD_REPLY;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.port.in.BlockReplyLetterUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.PushRecentReplyCachePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.application.response.ReplyLetterDetailResponse;
import online.bottler.letter.application.response.ReplyLetterResponse;
import online.bottler.letter.application.response.ReplyLetterSummaryResponse;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.DuplicateReplyLetterException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.notification.application.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyLetterServiceV2 implements ReplyLetterUseCase, BlockReplyLetterUseCase {

    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final PushRecentReplyCachePort pushRecentReplyCachePort;
    private final DeleteRecentReplyCachePort deleteRecentReplyCachePort;
    private final NotificationService notificationService;

    @Override
    public ReplyLetterResponse create(ReplyLetterCommand command) {
        if (checkIsReplied(command.letterId(), command.userId())) {
            throw new DuplicateReplyLetterException();
        }

        Letter letter = letterPersistencePort.loadById(command.letterId())
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));

        ReplyLetter replyLetter = replyLetterPersistencePort.create(
                ReplyLetter.create(command.userId(), command.letterContent(), command.letterId(), letter.getUserId()));

        letterBoxPersistencePort.createForReplyLetter(replyLetter.getLetterId(), replyLetter.getUserId(),
                replyLetter.getReceiverId(), replyLetter.getCreatedAt());

        pushRecentReplyCachePort.push(replyLetter.getId(), replyLetter.getLabel(), replyLetter.getReceiverId());
        notificationService.sendLetterNotification(KEYWORD_REPLY, replyLetter.getReceiverId(), replyLetter.getId(),
                replyLetter.getLabel());

        return ReplyLetterResponse.from(replyLetter);
    }

    @Override
    public Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery query) {
        validateLetterInUserBox(query.letterId(), query.userId());
        return replyLetterPersistencePort.loadSummariesByLetterIdAndReceiverId(query.letterId(), query.userId(),
                query.commonPageRequest().toPageable()).map(ReplyLetterSummaryResponse::from);
    }

    @Override
    public ReplyLetterDetailResponse getDetail(Long id, Long userId) {
        validateLetterInUserBox(id, userId);
        return ReplyLetterDetailResponse.from(findReplyLetter(id), checkIsReplied(id, userId));
    }

    @Override
    public void softDelete(ReplyLetterDeleteCommand command) {
        replyLetterPersistencePort.softDelete(command.id());
        letterBoxPersistencePort.delete(command.id(), LetterType.REPLY_LETTER, command.boxType());

        ReplyLetter replyLetter = findReplyLetter(command.id());
        deleteRecentReplyCachePort.delete(replyLetter.getReceiverId(), replyLetter.getId(), replyLetter.getLabel());
    }

    @Transactional
    @Override
    public Long softBlock(Long id) {
        replyLetterPersistencePort.softBlock(id);
        ReplyLetter replyLetter = findReplyLetter(id);
        return replyLetter.getUserId();
    }


    private boolean checkIsReplied(Long letterId, Long userId) {
        return replyLetterPersistencePort.existsByLetterIdAndUserId(letterId, userId);
    }

    private ReplyLetter findReplyLetter(Long id) {
        return replyLetterPersistencePort.loadById(id)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.REPLY_LETTER));
    }

    private void validateLetterInUserBox(Long letterId, Long userId) {
        //타입 구분 필요
        boolean isLetterInUserBox = letterBoxPersistencePort.existsByLetterIdAndUserId(letterId, userId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException();
        }
    }
}
