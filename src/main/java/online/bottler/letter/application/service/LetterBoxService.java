package online.bottler.letter.application.service;

import static online.bottler.letter.domain.BoxType.NONE;
import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;
import static online.bottler.letter.domain.LetterType.LETTER;
import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.application.strategy.LetterDeleteStrategy;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterDeleteKey;
import online.bottler.letter.domain.LetterDeleteValues;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.LetterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterBoxService implements LetterBoxUseCase {

    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final Map<LetterDeleteKey, LetterDeleteStrategy> strategyMap;

    @Transactional
    @Override
    public void save(List<Long> letterIds, Long userId) {
        letterBoxPersistencePort.createForDeveloperLetter(letterIds, userId);
    }

    @Transactional
    @Override
    public void createForLetter(Long letterId, Long userId, LocalDateTime localDateTime) {
        letterBoxPersistencePort.createForLetter(letterId, userId, localDateTime);
    }

    @Transactional
    @Override
    public void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt) {
        letterBoxPersistencePort.createForReplyLetter(letterId, userId, receiverId, createdAt);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummary> getAllLetters(CommonPageCommand commonPageCommand, Long userId) {
        return getLetterBoxSummaries(userId, commonPageCommand.toPageable(), NONE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummary> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId) {
        return getLetterBoxSummaries(userId, commonPageCommand.toPageable(), RECEIVE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LetterSummary> getSentLetters(CommonPageCommand commonPageCommand, Long userId) {
        return getLetterBoxSummaries(userId, commonPageCommand.toPageable(), SEND);
    }

    @Transactional
    @Override
    public void deleteLetter(Long letterId, LetterType letterType, BoxType boxType) {
        letterBoxPersistencePort.delete(letterId, letterType, boxType);
    }

    @Transactional
    @Override
    public void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId) {
        groupByLetterDeleteKey(letterDeleteCommands)
                .forEach((key, values) -> deleteLettersByLetterDeleteKey(key, values.letterIds(), userId));
    }

    @Transactional
    @Override
    public void deleteAllLetters(Long userId) {
        deleteAllLettersByBoxType(NONE, userId);
    }

    @Transactional
    @Override
    public void deleteAllReceivedLetters(Long userId) {
        deleteAllLettersByBoxType(RECEIVE, userId);
    }

    @Transactional
    @Override
    public void deleteAllSentLetters(Long userId) {
        deleteAllLettersByBoxType(SEND, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAccessDenied(Long letterId, Long userId) {
        return !letterBoxPersistencePort.existsByLetterIdAndUserId(letterId, userId);
    }

    private Page<LetterSummary> getLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxPersistencePort.loadLetterBoxSummaries(userId, pageable, boxType);
    }

    private Map<LetterDeleteKey, LetterDeleteValues> groupByLetterDeleteKey(
            List<LetterDeleteCommand> commands) {
        Map<LetterDeleteKey, LetterDeleteValues> groupedLetters = new HashMap<>();

        commands.stream()
                .filter(command -> command.letterType() != LetterType.NONE && command.boxType() != NONE)
                .forEach(command -> {
                    LetterDeleteKey key = LetterDeleteKey.of(command.letterType(), command.boxType());
                    groupedLetters.computeIfAbsent(key, k -> new LetterDeleteValues(new ArrayList<>()))
                            .letterIds().add(command.letterId());
                });

        return groupedLetters;
    }

    private void deleteLettersByLetterDeleteKey(LetterDeleteKey letterDeleteKey, List<Long> ids, Long userId) {
        getDeleteStrategy(letterDeleteKey).deleteLetters(ids, userId);
    }

    private void deleteAllLettersByBoxType(BoxType boxType, Long userId) {
        if (boxType == NONE || boxType == SEND) {
            deleteLettersForType(LetterDeleteKey.of(LETTER, boxType), userId);
            deleteLettersForType(LetterDeleteKey.of(REPLY_LETTER, boxType), userId);
        }

        if (boxType == NONE || boxType == RECEIVE) {
            letterBoxPersistencePort.deleteAllByUserIdAndBoxType(userId, RECEIVE);
        }
    }

    private void deleteLettersForType(LetterDeleteKey letterDeleteKey, Long userId) {
        List<Long> ids = getLetterIdsByLetterType(letterDeleteKey.letterType(), userId);
        if (!ids.isEmpty()) {
            getDeleteStrategy(letterDeleteKey).deleteLetters(ids, userId);
        }
    }

    private List<Long> getLetterIdsByLetterType(LetterType letterType, Long userId) {
        return switch (letterType) {
            case LETTER -> letterPersistencePort.loadIdsByUserId(userId);
            case REPLY_LETTER -> replyLetterPersistencePort.loadIdsByUserId(userId);
            default -> throw new IllegalArgumentException("Unsupported letter type");
        };
    }

    private LetterDeleteStrategy getDeleteStrategy(LetterDeleteKey key) {
        return strategyMap.get(key);
    }
}
