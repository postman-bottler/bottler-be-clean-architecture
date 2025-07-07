package online.bottler.letter.application.service;

import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
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
    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;

    @Transactional
    @Override
    public Letter write(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        return createLetterWithKeywords(letterWithKeywordsCommand);
    }

    @Transactional(readOnly = true)
    @Override
    public LetterWithKeywords getLetterWithKeywords(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        validateUserAccess(letterWithKeywordsDetailQuery.userId(), letterWithKeywordsDetailQuery.letterId());

        Letter letter = loadLetterById(letterWithKeywordsDetailQuery.letterId());

        List<String> letterKeywords = letterKeywordPersistencePort.loadKeywordsByLetterIdAndStatus(letter.getId(), LetterStatus.OPEN);

        return LetterWithKeywords.create(letter, letterKeywords);
    }

    @Transactional(readOnly = true)
    @Override
    public String getLabel(Long letterId) {
        return loadLetterById(letterId).getLabel();
    }

    @Transactional
    @Override
    public void delete(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        Letter letter = loadLetterById(letterWithKeywordsDeleteCommand.letterId());

        if (!letter.isOwner(letterWithKeywordsDeleteCommand.userId())) {
            throw new LetterAuthorMismatchException();
        }

        deleteLetterWithKeywords(letterWithKeywordsDeleteCommand);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Long> getLetterIdsByUserId(Long userId) {
        return letterPersistencePort.loadIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Letter> getLettersIncludingAllStatusByIdIn(List<Long> letterIds) {
        return letterPersistencePort.loadAllByIdIn(letterIds);
    }

    @Override
    public void softDeleteByIds(List<Long> ids) {
        List<Letter> letters = loadLetterByIds(ids);
        letters.forEach(Letter::delete);
        letterPersistencePort.createAll(letters);

        List<LetterKeyword> letterKeywords = letterKeywordPersistencePort.loadAllByLetterIdInAndStatus(ids, LetterStatus.OPEN);
        letterKeywords.forEach(LetterKeyword::delete);
        letterKeywordPersistencePort.createAll(letterKeywords);
    }

    @Transactional
    @Override
    public Long softBlock(Long letterId) {
        Letter letter = loadLetterById(letterId);
        letter.block();
        letterPersistencePort.create(letter);

        List<LetterKeyword> letterKeywords = letterKeywordPersistencePort.loadAllByLetterId(letter.getId());
        letterKeywords.forEach(LetterKeyword::delete);
        letterKeywordPersistencePort.createAll(letterKeywords);

        return letter.getUserId();
    }

    private Letter createLetterWithKeywords(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = letterPersistencePort.create(letterWithKeywordsCommand.toLetter());
        letterKeywordPersistencePort.createAll(
                LetterKeyword.createList(letter.getId(), letterWithKeywordsCommand.toKeywords()));
        return letter;
    }

    private Letter loadLetterById(Long letterId) {
        return letterPersistencePort.loadByIdAndStatus(letterId, LetterStatus.OPEN)
                .orElseThrow(() -> new LetterNotFoundException(LETTER));
    }

    private List<Letter> loadLetterByIds(List<Long> ids) {
        return letterPersistencePort.loadAllByIdInAndStatus(ids, LetterStatus.OPEN);
    }


    private void validateUserAccess(Long userId, Long letterId) {
        if (!isLetterInBox(userId, letterId)) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private boolean isLetterInBox(Long userId, Long letterId) {
        return letterBoxPersistencePort.existsByUserIdAndLetterId(userId, letterId);
    }

    private void deleteLetterWithKeywords(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        Letter letter = loadLetterById(letterWithKeywordsDeleteCommand.letterId());
        letter.delete();
        letterPersistencePort.create(letter);

        List<LetterKeyword> letterKeywords = letterKeywordPersistencePort.loadAllByLetterId(letter.getId());
        letterKeywords.forEach(LetterKeyword::delete);
        letterKeywordPersistencePort.createAll(letterKeywords);
    }
}
