package online.bottler.letter.application.service;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
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
        return getLetterBoxSummaries(userId, commonPageCommand.toPageable(), null);
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

    @Override
    @Transactional
    public void removeLetterFromBox(Long letterId, LetterBoxType letterBoxType) {
        deleteLettersFromBox(null, List.of(letterId), letterBoxType);
    }

    @Override
    @Transactional
    public void removeLettersFromBox(List<Long> letterIds, LetterBoxType letterBoxType) {
        deleteLettersFromBox(null, letterIds, letterBoxType);
    }

    @Override
    @Transactional
    public void removeLettersFromBox(Long userId, LetterBoxType letterBoxType) {
        deleteLettersFromBox(userId, null, letterBoxType);
    }

    @Override
    @Transactional
    public void removeLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType) {
        deleteLettersFromBox(userId, letterIds, letterBoxType);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAccessDenied(Long letterId, Long userId) {
        return !letterBoxPersistencePort.existsByLetterIdAndUserId(letterId, userId);
    }

    private Page<LetterSummary> getLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxPersistencePort.loadLetterBoxSummaries(userId, pageable, boxType);
    }

    private void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType) {
        letterBoxPersistencePort.deleteLettersFromBox(userId, letterIds, letterBoxType);
    }
}
