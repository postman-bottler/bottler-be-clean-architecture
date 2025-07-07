package online.bottler.letter.application.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterBoxService implements LetterBoxUseCase {

    private final LetterBoxPersistencePort letterBoxPersistencePort;

    @Override
    public void archiveLetter(Letter letter) {

    }

    @Override
    public void archiveLetter(ReplyLetter replyLetter) {

    }

    @Override
    public void archiveLetters(List<Long> letterId, Long userId) {

    }

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

    @Override
    @Transactional(readOnly = true)
    public Page<LetterSummary> getLetterBoxSummaries(Long userId, BoxType boxType,
                                                     CommonPageCommand commonPageCommand) {
        return letterBoxPersistencePort.loadLetterBoxSummaries(userId, boxType, commonPageCommand.toPageable());
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
        return !letterBoxPersistencePort.existsByUserIdAndLetterId(userId, letterId);
    }

    private void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType) {
        letterBoxPersistencePort.deleteLettersFromBox(userId, letterIds, letterBoxType);
    }
}
