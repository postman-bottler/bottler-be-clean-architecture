package online.bottler.letter.application.service;

import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.BlockLetterUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements LetterWithKeywordsUseCase, BlockLetterUseCase {

    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    @Override
    @Transactional
    public Letter write(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        return createLetterWithKeywords(letterWithKeywordsCommand);
    }

    @Override
    @Transactional(readOnly = true)
    public LetterWithKeywords getLetterWithKeywords(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        validateUserAccess(letterWithKeywordsDetailQuery.userId(), letterWithKeywordsDetailQuery.letterId());

        Letter letter = loadLetterById(letterWithKeywordsDetailQuery.letterId());

        List<String> letterKeywords = letterKeywordPersistencePort.loadKeywordsByLetterIdAndStatus(letter.getId(), LetterStatus.OPEN);

        return LetterWithKeywords.create(letter, letterKeywords);
    }

    @Override
    @Transactional(readOnly = true)
    public String getLabel(Long letterId) {
        return loadLetterById(letterId).getLabel();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getLetterIdsByUserId(Long userId) {
        return letterPersistencePort.loadIdsByUserIdAndStatus(userId, LetterStatus.OPEN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Letter> getLettersIncludingAllStatusByIdIn(List<Long> letterIds) {
        return letterPersistencePort.loadAllByIdIn(letterIds);
    }

    @Override
    @Transactional
    public void deleteLetter(Long userId, Long letterId) {
        deleteLetterWithKeywords(userId, List.of(letterId));
    }

    @Override
    @Transactional
    public void deleteLetters(Long userId, List<Long> letterIds) {
        deleteLetterWithKeywords(userId, letterIds);
    }

    @Override
    @Transactional
    public Long blockLetter(Long letterId) {
        Letter letter = loadLetterById(letterId);
        letter.block();
        letterPersistencePort.save(letter);

        List<LetterKeyword> letterKeywords = letterKeywordPersistencePort.loadAllByLetterId(letterId);
        letterKeywords.forEach(LetterKeyword::block);
        letterKeywordPersistencePort.saveAll(letterKeywords);

        return letter.getUserId();
    }

    private Letter createLetterWithKeywords(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = letterPersistencePort.save(letterWithKeywordsCommand.toLetter());

        letterKeywordPersistencePort.saveAll(
                LetterKeyword.createList(letter.getId(), letterWithKeywordsCommand.toKeywords())
        );

        return letter;
    }

    private void validateUserAccess(Long userId, Long letterId) {
        if (!isLetterInBox(userId, letterId)) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private boolean isLetterInBox(Long userId, Long letterId) {
        return letterBoxPersistencePort.existsByUserIdAndLetterId(userId, letterId);
    }

    private Letter loadLetterById(Long letterId) {
        return letterPersistencePort.loadByIdAndStatus(letterId, LetterStatus.OPEN)
                .orElseThrow(() -> new LetterNotFoundException(LETTER));
    }

    private List<Letter> loadLetterByIds(List<Long> ids) {
        return letterPersistencePort.loadAllByIdInAndStatus(ids, LetterStatus.OPEN);
    }

    private void deleteLetterWithKeywords(Long userId, List<Long> letterIds) {
        List<Letter> letters = loadLetterByIds(letterIds);

        validateOwnerShip(userId, letters);

        letters.forEach(Letter::delete);
        letterPersistencePort.saveAll(letters);

        List<LetterKeyword> letterKeywords = letterKeywordPersistencePort.loadAllByLetterIdInAndStatus(letterIds, LetterStatus.OPEN);
        letterKeywords.forEach(LetterKeyword::delete);
        letterKeywordPersistencePort.saveAll(letterKeywords);
    }

    private void validateOwnerShip(Long userId, List<Letter> letters) {
        if (!letters.stream().allMatch(letter -> letter.isOwner(userId))) {
            throw new LetterAuthorMismatchException();
        }
    }
}
