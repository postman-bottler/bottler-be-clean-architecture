package online.bottler.letter.application.service;

import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.BlockLetterUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements LetterWithKeywordsUseCase, BlockLetterUseCase {

    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;

    @Transactional
    @Override
    public Letter create(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        return createLetterWithKeywords(letterWithKeywordsCommand);
    }

    @Transactional(readOnly = true)
    @Override
    public LetterWithKeywords get(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        Letter letter = loadLetterById(letterWithKeywordsDetailQuery.letterId());
        List<LetterKeyword> keywords = letterKeywordPersistencePort.loadKeywordsByLetterId(
                letterWithKeywordsDetailQuery.letterId());
        return LetterWithKeywords.create(letter, keywords.stream().map(LetterKeyword::getKeyword).toList());
    }

    @Transactional(readOnly = true)
    @Override
    public String getLabel(Long letterId) {
        return loadLetterById(letterId).getLabel();
    }

    @Transactional
    @Override
    public void delete(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        Optional<Letter> letter = letterPersistencePort.loadById(letterWithKeywordsDeleteCommand.letterId());
        validateLetterOwnerShip(letter, letterWithKeywordsDeleteCommand.userId());
        deleteLetterWithKeywords(letterWithKeywordsDeleteCommand);
    }

    @Transactional(readOnly = true)
    @Override
    public Letter getLetter(Long letterId) {
        return loadLetterById(letterId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Long> getLetterIdsByUserId(Long userId) {
        return letterPersistencePort.loadIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Letter> loadAllIncludingDeletedByIds(List<Long> letterIds) {
        return letterPersistencePort.loadAllIncludingDeletedByIds(letterIds);
    }

    @Transactional
    @Override
    public Long softBlock(Long letterId) {
        Letter letter = loadLetterById(letterId);
        letterPersistencePort.softBlock(loadLetterById(letterId).getId());
        return letter.getUserId();
    }

    private Letter createLetterWithKeywords(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = letterPersistencePort.create(letterWithKeywordsCommand.toLetter());
        letterKeywordPersistencePort.createAll(
                LetterKeyword.createList(letter.getId(), letterWithKeywordsCommand.toKeywords()));
        return letter;
    }

    private Letter loadLetterById(Long letterId) {
        return letterPersistencePort.loadById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LETTER));
    }

    private void deleteLetterWithKeywords(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        letterPersistencePort.softDelete(letterWithKeywordsDeleteCommand.letterId());
        letterKeywordPersistencePort.softDelete(letterWithKeywordsDeleteCommand.letterId());
    }

    private void validateLetterOwnerShip(Optional<Letter> letter, Long userId) {
        letter.filter(l -> l.getUserId().equals(userId)).orElseThrow(LetterAuthorMismatchException::new);
    }
}
