package online.bottler.letter.application;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.in.web.request.CommonPageRequest;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.InvalidLetterRequestException;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterBoxService implements LetterBoxUseCase {

    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final DeleteRecentReplyCachePort deleteRecentReplyCachePort;

    @Transactional
    @Override
    public void save(List<Long> letterIds, Long userId) {
        letterBoxPersistencePort.createForDeveloperLetter(letterIds, userId);
    }

    @Override
    public Page<LetterSummaryResponse> getAllLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.NONE);
    }

    @Override
    public Page<LetterSummaryResponse> getReceivedLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.RECEIVE);
    }

    @Override
    public Page<LetterSummaryResponse> getSentLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.SEND);
    }

    @Transactional
    @Override
    public void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId) {
        Map<LetterType, Map<BoxType, List<Long>>> groupedRequests = LetterDeleteCommand.groupByTypeAndBox(
                letterDeleteCommands);
        groupedRequests.forEach(
                (type, boxMap) -> boxMap.forEach((box, ids) -> deleteLettersByType(box, type, ids, userId)));
    }

    private void deleteLettersByType(BoxType boxType, LetterType letterType, List<Long> ids, Long userId) {
        switch (letterType) {
            case LETTER -> deleteLetter(boxType, ids, userId);
            case REPLY_LETTER -> deleteReplyLetter(boxType, ids, userId);
        }
    }

    private void deleteLetter(BoxType boxType, List<Long> ids, Long userId) {
        validateLetterOwnerShip(userId, letterPersistencePort.loadAllByIds(ids));
        switch (boxType) {
            case SEND -> {
                letterPersistencePort.softDeleteByIds(ids);
                letterKeywordPersistencePort.softDeleteByIds(ids);
                letterBoxPersistencePort.deleteByCondition(ids, LetterType.LETTER, BoxType.NONE);
            }
            case RECEIVE ->
                    letterBoxPersistencePort.deleteByConditionAndUserId(ids, LetterType.LETTER, boxType, userId);
        }
    }

    private void validateLetterOwnerShip(Long userId, List<Letter> letters) {
        if (letters.stream().anyMatch(letter -> !letter.getUserId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }
    }

    private void deleteReplyLetter(BoxType boxType, List<Long> ids, Long userId) {
        switch (boxType) {
            case SEND -> {
                if (ids == null || ids.isEmpty()) {
                    throw new InvalidLetterRequestException("삭제할 답장 편지 ID 목록이 비어 있습니다.");
                }

                List<ReplyLetter> replyLetters = replyLetterPersistencePort.loadAllByIds(ids);

                if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.getUserId().equals(userId))) {
                    throw new LetterAuthorMismatchException();
                }

                replyLetters.forEach(replyLetter -> deleteRecentReplyCachePort.delete(replyLetter.getReceiverId(),
                        replyLetter.getId(), replyLetter.getLabel()));

                replyLetterPersistencePort.softDeleteByIds(ids);

                letterBoxPersistencePort.deleteByCondition(ids, LetterType.REPLY_LETTER, BoxType.NONE);
            }
            case RECEIVE ->
                    letterBoxPersistencePort.deleteByConditionAndUserId(ids, LetterType.REPLY_LETTER, boxType,
                            userId);
        }
    }


    @Transactional
    @Override
    public void deleteAllLetters(Long userId) {
        deleteAllLettersByBoxType(BoxType.NONE, userId);
    }

    @Transactional
    @Override
    public void deleteAllReceivedLetters(Long userId) {
        deleteAllLettersByBoxType(BoxType.RECEIVE, userId);
    }

    @Transactional
    @Override
    public void deleteAllSentLetters(Long userId) {
        deleteAllLettersByBoxType(BoxType.SEND, userId);
    }

    private void deleteAllLettersByBoxType(BoxType boxType, Long userId) {
        if (boxType == BoxType.NONE || boxType == BoxType.SEND) {
            List<Long> letterIds = letterPersistencePort.loadIdsByUserId(userId);
            List<Long> replyIds = replyLetterPersistencePort.loadIdsByUserId(userId);

            if (!letterIds.isEmpty()) {
                letterPersistencePort.softDeleteByIds(letterIds);
                letterKeywordPersistencePort.softDeleteByIds(letterIds);
                letterBoxPersistencePort.deleteByCondition(letterIds, LetterType.LETTER, BoxType.NONE);
            }

            if (!replyIds.isEmpty()) {
                replyLetterPersistencePort.softDeleteByIds(replyIds);
                letterBoxPersistencePort.deleteByCondition(replyIds, LetterType.REPLY_LETTER, BoxType.NONE);
            }
        }

        if (boxType == BoxType.NONE || boxType == BoxType.RECEIVE) {
            letterBoxPersistencePort.deleteAllByUserIdAndBoxType(userId, BoxType.RECEIVE);
        }
    }

    private Page<LetterSummaryResponse> getLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        List<LetterSummaryResponse> responses = LetterSummaryResponse.fromList(
                letterBoxPersistencePort.loadLetterBoxSummaries(userId, pageable, boxType));
        return new PageImpl<>(responses, pageable, countLetters(userId, boxType));
    }

    private long countLetters(Long userId, BoxType boxType) {
        return letterBoxPersistencePort.countLetters(userId, boxType);
    }
}
