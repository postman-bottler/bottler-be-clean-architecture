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
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand command) {
        Letter letter = letterPersistencePort.create(command.toLetter());

        List<LetterKeyword> letterKeywords = LetterKeyword.createList(letter.getId(), command.toKeywords());
        letterKeywordPersistencePort.createAll(letterKeywords);

        letterBoxPersistencePort.createForLetter(letter.getId(), letter.getUserId(), letter.getCreatedAt());

        return LetterWithKeywordsResponse.from(LetterWithKeywords.create(letter, command.keywords()));
    }

    @Transactional(readOnly = true)
    @Override
    public LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery query) {
        if (!letterBoxPersistencePort.existsByLetterIdAndUserId(query.letterId(), query.userId())) {
            throw new UnauthorizedLetterAccessException();
        }

        boolean isReplied = replyLetterPersistencePort.existsByLetterIdAndUserId(query.letterId(), query.userId());
        List<LetterKeyword> keywords = letterKeywordPersistencePort.loadKeywordsByLetterId(query.letterId());
        String profile = userPersistencePort.findById(query.userId()).getImageUrl();
        Letter letter = letterPersistencePort.loadById(query.letterId())
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));

        return LetterWithKeywordsDetailResponse.of(letter, keywords, query.userId(), profile, isReplied);
    }

    @Transactional(readOnly = true)
    @Override
    public String getLabel(Long letterId) {
        return letterPersistencePort.loadById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER)).getLabel();
    }

    @Transactional
    @Override
    public void delete(LetterWithKeywordsDeleteCommand command) {
        Optional<Letter> letter = letterPersistencePort.loadById(command.letterId());
        validateLetterOwnerShip(letter, command.userId());

        letterPersistencePort.softDelete(command.letterId(), command.userId(), command.boxType());
        letterKeywordPersistencePort.softDelete(command.letterId());
        letterBoxPersistencePort.delete(command.letterId(), LetterType.LETTER, BoxType.NONE);
    }

    @Override
    public Long softBlock(Long letterId) {
        Letter letter = letterPersistencePort.loadById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));
        letterPersistencePort.softBlock(letter.getId());
        return letter.getUserId();
    }

    private void validateLetterOwnerShip(Optional<Letter> letter, Long userId) {
        if (letter.isPresent() && letter.get().getUserId().equals(userId)) {
            throw new LetterAuthorMismatchException();
        }
    }
}
