package online.bottler.letter.v2.application;


import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.letter.v2.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.v2.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.v2.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.v2.application.port.in.BlockLetterUseCase;
import online.bottler.letter.v2.application.port.in.CreateLetterWithKeywordsUseCase;
import online.bottler.letter.v2.application.port.in.DeleteLetterWithKeywordsUseCase;
import online.bottler.letter.v2.application.port.in.GetLetterUseCase;
import online.bottler.letter.v2.application.port.in.GetLetterWithKeywordsDetailUseCase;
import online.bottler.letter.v2.application.port.out.CheckLetterBoxPersistencePort;
import online.bottler.letter.v2.application.port.out.CheckReplyLetterPersistencePort;
import online.bottler.letter.v2.application.port.out.CreateLetterBoxPersistencePort;
import online.bottler.letter.v2.application.port.out.CreateLetterKeywordPersistencePort;
import online.bottler.letter.v2.application.port.out.CreateLetterPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterBoxPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterKeywordPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterPersistencePort;
import online.bottler.letter.v2.application.port.out.LoadLetterKeywordPersistencePort;
import online.bottler.letter.v2.application.port.out.LoadLetterPersistencePort;
import online.bottler.letter.v2.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.v2.application.response.LetterWithKeywordsResponse;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements CreateLetterWithKeywordsUseCase, GetLetterWithKeywordsDetailUseCase,
        DeleteLetterWithKeywordsUseCase, GetLetterUseCase, BlockLetterUseCase {

    private final CreateLetterPersistencePort createLetterPersistencePort;
    private final CreateLetterKeywordPersistencePort createLetterKeywordPersistencePort;
    private final CreateLetterBoxPersistencePort createLetterBoxPersistencePort;
    private final CheckLetterBoxPersistencePort checkLetterBoxPersistencePort;
    private final CheckReplyLetterPersistencePort checkReplyLetterPersistencePort;
    private final LoadLetterKeywordPersistencePort loadLetterKeywordPersistencePort;
    private final LoadLetterPersistencePort loadLetterPersistencePort;
    private final DeleteLetterPersistencePort deleteLetterPersistencePort;
    private final DeleteLetterKeywordPersistencePort deleteLetterKeywordPersistencePort;
    private final DeleteLetterBoxPersistencePort deleteLetterBoxPersistencePort;
    private final UserUseCase userUseCase;

    @Override
    @Transactional
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand command) {
        Letter letter = createLetterPersistencePort.create(command.toLetter());

        List<LetterKeyword> letterKeywords = LetterKeyword.createList(letter.getId(),
                command.toKeywords());
        createLetterKeywordPersistencePort.createAll(letterKeywords);

        createLetterBoxPersistencePort.createForLetter(letter.getId(), letter.getUserId(), letter.getCreatedAt());

        return LetterWithKeywordsResponse.from(LetterWithKeywords.create(letter, command.keywords()));
    }

    @Override
    @Transactional(readOnly = true)
    public LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery query) {
        if (!checkLetterBoxPersistencePort.existsByLetterIdAndUserId(query.letterId(), query.userId())) {
            throw new UnauthorizedLetterAccessException();
        }

        boolean isReplied = checkReplyLetterPersistencePort.existsByLetterIdAndUserId(query.letterId(), query.userId());
        List<LetterKeyword> keywords = loadLetterKeywordPersistencePort.loadKeywordsByLetterId(query.letterId());
        String profile = userUseCase.findById(query.userId()).getImageUrl();
        Letter letter = loadLetterPersistencePort.loadById(query.letterId())
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));

        return LetterWithKeywordsDetailResponse.of(letter, keywords, query.userId(), profile, isReplied);
    }

    @Override
    @Transactional
    public void delete(LetterWithKeywordsDeleteCommand command) {
        deleteLetterPersistencePort.softDelete(command.letterId(), command.userId(), command.boxType());
        deleteLetterKeywordPersistencePort.softDelete(command.letterId());
        deleteLetterBoxPersistencePort.delete(command.letterId(), LetterType.LETTER, BoxType.NONE);
    }

    @Override
    public Letter getLetter(String letterId) {
        return null;
    }

    @Override
    public Long softBlock(Long letterId) {
        return 0L;
    }
}
