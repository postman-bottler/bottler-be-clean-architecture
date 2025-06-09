package online.bottler.letter.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.BlockLetterUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.user.application.port.out.UserPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements LetterWithKeywordsUseCase, BlockLetterUseCase {

    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final UserPersistencePort userPersistencePort;

    @Transactional
    @Override
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = createLetterWithKeywords(letterWithKeywordsCommand);
        createLetterBox(letter);
        return LetterWithKeywordsResponse.from(LetterWithKeywords.create(letter, letterWithKeywordsCommand.keywords()));
    }

    @Transactional(readOnly = true)
    @Override
    public LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        validateUserPermission(letterWithKeywordsDetailQuery);

        Letter letter = loadLetterById(letterWithKeywordsDetailQuery.letterId());

        List<LetterKeyword> keywords = letterKeywordPersistencePort.loadKeywordsByLetterId(
                letterWithKeywordsDetailQuery.letterId());

        String profile = userPersistencePort.findById(letterWithKeywordsDetailQuery.userId()).getImageUrl();

        boolean isReplied = replyLetterPersistencePort.existsByLetterIdAndUserId(
                letterWithKeywordsDetailQuery.letterId(), letterWithKeywordsDetailQuery.userId());

        return LetterWithKeywordsDetailResponse.of(letter, keywords, letterWithKeywordsDetailQuery.userId(), profile,
                isReplied);
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
        deleteLetterBox(letterWithKeywordsDeleteCommand);
    }

    @Transactional
    @Override
    public Long softBlock(Long letterId) {
        Letter letter = loadLetterById(letterId);
        letterPersistencePort.softBlock(letter.getId());
        return letter.getUserId();
    }

    private Letter createLetterWithKeywords(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = letterPersistencePort.create(letterWithKeywordsCommand.toLetter());
        letterKeywordPersistencePort.createAll(
                LetterKeyword.createList(letter.getId(), letterWithKeywordsCommand.toKeywords()));
        return letter;
    }

    private void createLetterBox(Letter letter) {
        letterBoxPersistencePort.createForLetter(letter.getId(), letter.getUserId(), letter.getCreatedAt());
    }

    private Letter loadLetterById(Long letterId) {
        return letterPersistencePort.loadById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));
    }

    private void validateUserPermission(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        if (!letterBoxPersistencePort.existsByLetterIdAndUserId(letterWithKeywordsDetailQuery.letterId(),
                letterWithKeywordsDetailQuery.userId())) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private void deleteLetterBox(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        letterBoxPersistencePort.delete(letterWithKeywordsDeleteCommand.letterId(), LetterType.LETTER, BoxType.NONE);
    }

    private void deleteLetterWithKeywords(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        letterPersistencePort.softDelete(letterWithKeywordsDeleteCommand.letterId());
        letterKeywordPersistencePort.softDelete(letterWithKeywordsDeleteCommand.letterId());
    }

    private void validateLetterOwnerShip(Optional<Letter> letter, Long userId) {
        letter.filter(l -> l.getUserId().equals(userId)).orElseThrow(LetterAuthorMismatchException::new);
    }
}
