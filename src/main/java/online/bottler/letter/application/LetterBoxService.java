package online.bottler.letter.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import online.bottler.letter.adapter.in.web.request.CommonPageRequest;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.strategy.LetterDeleteStrategy;
import online.bottler.letter.application.strategy.LetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.LetterDeleteStrategySend;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategySend;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterDeleteKey;
import online.bottler.letter.domain.LetterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LetterBoxService implements LetterBoxUseCase {

    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final ReplyLetterPersistencePort replyLetterPersistencePort;

    private final Map<BoxType, LetterDeleteStrategy> letterDeleteStrategyMap;
    private final Map<BoxType, LetterDeleteStrategy> replyLetterDeleteStrategyMap;

    public LetterBoxService(LetterBoxPersistencePort letterBoxPersistencePort,
                            LetterPersistencePort letterPersistencePort,
                            ReplyLetterPersistencePort replyLetterPersistencePort,
                            LetterKeywordPersistencePort letterKeywordPersistencePort,
                            DeleteRecentReplyCachePort deleteRecentReplyCachePort) {
        this.letterBoxPersistencePort = letterBoxPersistencePort;
        this.letterPersistencePort = letterPersistencePort;
        this.replyLetterPersistencePort = replyLetterPersistencePort;

        this.letterDeleteStrategyMap = new HashMap<>();
        letterDeleteStrategyMap.put(BoxType.SEND,
                new LetterDeleteStrategySend(letterPersistencePort, letterKeywordPersistencePort,
                        letterBoxPersistencePort));
        letterDeleteStrategyMap.put(BoxType.RECEIVE, new LetterDeleteStrategyReceive(letterBoxPersistencePort));

        this.replyLetterDeleteStrategyMap = new HashMap<>();
        replyLetterDeleteStrategyMap.put(BoxType.SEND,
                new ReplyLetterDeleteStrategySend(replyLetterPersistencePort, letterBoxPersistencePort,
                        deleteRecentReplyCachePort));
        replyLetterDeleteStrategyMap.put(BoxType.RECEIVE,
                new ReplyLetterDeleteStrategyReceive(letterBoxPersistencePort));
    }

    @Transactional
    @Override
    public void save(List<Long> letterIds, Long userId) {
        letterBoxPersistencePort.createForDeveloperLetter(letterIds, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummaryResponse> getAllLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.NONE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummaryResponse> getReceivedLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.RECEIVE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummaryResponse> getSentLetters(CommonPageRequest commonPageRequest, Long userId) {
        return getLetterBoxSummaries(userId, commonPageRequest.toPageable(), BoxType.SEND);
    }

    @Transactional
    @Override
    public void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId) {
        LetterDeleteCommand.groupByLetterTypeAndBoxType(letterDeleteCommands)
                .forEach((key, values) -> deleteLettersByLetterTypeAndBoxType(key, values.letterIds(), userId));
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

    private Page<LetterSummaryResponse> getLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        List<LetterSummaryResponse> responses = LetterSummaryResponse.fromList(
                letterBoxPersistencePort.loadLetterBoxSummaries(userId, pageable, boxType));
        return new PageImpl<>(responses, pageable, countLetters(userId, boxType));
    }

    private long countLetters(Long userId, BoxType boxType) {
        return letterBoxPersistencePort.countLetters(userId, boxType);
    }

    private void deleteLettersByLetterTypeAndBoxType(LetterDeleteKey key, List<Long> ids, Long userId) {
        getDeleteStrategy(key.letterType(), key.boxType()).deleteLetters(ids, userId);
    }

    private void deleteAllLettersByBoxType(BoxType boxType, Long userId) {
        if (boxType == BoxType.NONE || boxType == BoxType.SEND) {
            deleteLettersForType(LetterType.LETTER, boxType, userId);
            deleteLettersForType(LetterType.REPLY_LETTER, boxType, userId);
        }

        if (boxType == BoxType.NONE || boxType == BoxType.RECEIVE) {
            letterBoxPersistencePort.deleteAllByUserIdAndBoxType(userId, BoxType.RECEIVE);
        }
    }

    private void deleteLettersForType(LetterType letterType, BoxType boxType, Long userId) {
        List<Long> ids = getLetterIdsByType(letterType, userId);
        if (!ids.isEmpty()) {
            getDeleteStrategy(letterType, boxType).deleteLetters(ids, userId);
        }
    }

    private List<Long> getLetterIdsByType(LetterType letterType, Long userId) {
        return switch (letterType) {
            case LETTER -> letterPersistencePort.loadIdsByUserId(userId);
            case REPLY_LETTER -> replyLetterPersistencePort.loadIdsByUserId(userId);
            default -> throw new IllegalArgumentException("Unsupported letter type");
        };
    }

    private LetterDeleteStrategy getDeleteStrategy(LetterType letterType, BoxType boxType) {
        return switch (letterType) {
            case LETTER -> letterDeleteStrategyMap.get(boxType);
            case REPLY_LETTER -> replyLetterDeleteStrategyMap.get(boxType);
            default -> throw new IllegalArgumentException("Unsupported letter type");
        };
    }
}
