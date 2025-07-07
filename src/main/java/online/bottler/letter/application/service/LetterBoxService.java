package online.bottler.letter.application.service;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;
import static online.bottler.letter.domain.LetterType.LETTER;
import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterBox;
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
    @Transactional
    public void archiveLetter(Letter letter) {
        archiveLetterToBox(letter.getId(), letter.getUserId(), LetterBoxType.of(LETTER, SEND));
    }

    @Override
    @Transactional
    public void archiveLetter(ReplyLetter replyLetter) {
        archiveLetterToBox(replyLetter.getSenderId(), replyLetter.getLetterId(), LetterBoxType.of(REPLY_LETTER, SEND));
        archiveLetterToBox(replyLetter.getReceiverId(), replyLetter.getLetterId(), LetterBoxType.of(REPLY_LETTER, RECEIVE));
    }

    @Override
    @Transactional
    public void archiveLetters(List<Long> letterIds, Long userId) {
        letterIds.forEach(letterId -> archiveLetterToBox(userId, letterId, LetterBoxType.of(LETTER, RECEIVE)));
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

    private void archiveLetterToBox(Long userId, Long letterId, LetterBoxType letterBoxType) {
        letterBoxPersistencePort.save(LetterBox.create(userId, letterId, letterBoxType));
    }

    private void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType) {
        letterBoxPersistencePort.deleteLettersFromBox(userId, letterIds, letterBoxType);
    }
}
