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
    private final LetterWithKeywordsUseCase letterWithKeywordsUseCase;
    private final ReplyLetterUseCase replyLetterUseCase;

    private final Map<LetterBoxType, LetterDeleteStrategy> letterDeleteStrategyMap;

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getLetters(Long userId, String boxType, CommonPageCommand commonPageCommand) {
        return letterBoxUseCase.getLetterBoxSummaries(userId, getBoxType(boxType), commonPageCommand)
                .map(LetterSummaryResponse::from);
    }

    @Transactional
    public void deleteLetters(Long userId, List<LetterDeleteCommand> letterDeleteCommands) {
        Map<LetterBoxType, LetterDeletion> letterDeleteMap = LetterDeleteCommand.toLetterDeleteMap(
                letterDeleteCommands);

        letterDeleteMap.forEach((key, value) -> getDeleteStrategy(key).deleteLetters(userId, value.letterIds()));
    }

    @Transactional
    public void deleteAllLetters(Long userId, String boxType) {
        BoxType boxTypeEnum = getBoxType(boxType);

        if (boxTypeEnum == null || boxTypeEnum == SEND) {
            deleteLettersForType(userId, LetterBoxType.of(LETTER, boxTypeEnum));
            deleteLettersForType(userId, LetterBoxType.of(REPLY_LETTER, boxTypeEnum));
        }

        if (boxTypeEnum == null || boxTypeEnum == RECEIVE) {
            letterBoxUseCase.removeLettersFromBox(userId, LetterBoxType.of(null, boxTypeEnum));
        }
    }

    private BoxType getBoxType(String boxType) {
        try {
            return (boxType != null) ? BoxType.valueOf(boxType.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid box type: " + boxType);
        }
    }

    private void deleteLettersForType(Long userId, LetterBoxType letterBoxType) {
        List<Long> letterIds = getLetterIdsByLetterType(userId, letterBoxType.getLetterType());

        if (!letterIds.isEmpty()) {
            getDeleteStrategy(letterBoxType).deleteLetters(userId, letterIds);
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
