package online.bottler.letter.application.service;

import static online.bottler.letter.domain.BoxType.NONE;
import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
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

    @Transactional(readOnly = true)
    @Override
    public boolean isAccessDenied(Long letterId, Long userId) {
        return !letterBoxPersistencePort.existsByLetterIdAndUserId(letterId, userId);
    }

    private Page<LetterSummary> getLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxPersistencePort.loadLetterBoxSummaries(userId, pageable, boxType);
    }

    @Override
    public void deleteAllByUserIdAndBoxType(Long userId, BoxType boxType) {
        letterBoxPersistencePort.deleteAllByUserIdAndBoxType(userId, boxType);
    }

    @Override
    public void deleteByTypeAndUserId(List<Long> ids, LetterType letterType, BoxType boxType, Long userId) {
        letterBoxPersistencePort.deleteByConditionAndUserId(ids, letterType, boxType, userId);
    }

    @Override
    public void deleteByType(List<Long> ids, LetterType letterType, BoxType boxType) {
        letterBoxPersistencePort.deleteByCondition(ids, letterType, boxType);
    }
}
