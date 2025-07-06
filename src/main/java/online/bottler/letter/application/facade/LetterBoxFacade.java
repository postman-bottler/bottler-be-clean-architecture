package online.bottler.letter.application.facade;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;
import static online.bottler.letter.domain.LetterType.LETTER;
import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.strategy.LetterDeleteStrategy;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterDeletion;
import online.bottler.letter.domain.LetterType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LetterBoxFacade {

    private final LetterBoxUseCase letterBoxUseCase;
    private final Map<LetterBoxType, LetterDeleteStrategy> letterDeleteStrategyMap;
    private final LetterWithKeywordsUseCase letterWithKeywordsUseCase;
    private final ReplyLetterUseCase replyLetterUseCase;

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getAllLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getAllLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getReceivedLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getSentLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getSentLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional
    public void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId) {
        Map<LetterBoxType, LetterDeletion> letterDeleteMap = LetterDeleteCommand.toLetterDeleteMap(
                letterDeleteCommands);

        letterDeleteMap.forEach((key, value) -> getDeleteStrategy(key).deleteLetters(value.letterIds(), userId));
    }

    @Transactional
    public void deleteAllLetters(Long userId, BoxType boxType) {
        if (boxType == null || boxType == SEND) {
            deleteLettersForType(userId, LetterBoxType.of(LETTER, boxType));
            deleteLettersForType(userId, LetterBoxType.of(REPLY_LETTER, boxType));
        }

        if (boxType == null || boxType == RECEIVE) {
            letterBoxUseCase.removeLettersFromBox(userId, LetterBoxType.of(null, RECEIVE));
        }
    }

    private void deleteLettersForType(Long userId, LetterBoxType letterBoxType) {
        List<Long> ids = getLetterIdsByLetterType(userId, letterBoxType.getLetterType());
        if (!ids.isEmpty()) {
            getDeleteStrategy(letterBoxType).deleteLetters(ids, userId);
        }
    }

    private List<Long> getLetterIdsByLetterType(Long userId, LetterType letterType) {
        return switch (letterType) {
            case LETTER -> letterWithKeywordsUseCase.getLetterIdsByUserId(userId);
            case REPLY_LETTER -> replyLetterUseCase.getIdsByUserId(userId);
        };
    }

    private LetterDeleteStrategy getDeleteStrategy(LetterBoxType letterBoxType) {
        return letterDeleteStrategyMap.get(letterBoxType);
    }
}
