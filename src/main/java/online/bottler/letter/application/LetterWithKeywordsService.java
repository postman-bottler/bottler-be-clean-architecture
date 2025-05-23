package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.CreateLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.DeleteLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.GetLetterWithKeywordsDetailUseCase;
import online.bottler.letter.application.port.out.CheckLetterBoxPort;
import online.bottler.letter.application.port.out.CheckReplyLetterPort;
import online.bottler.letter.application.port.out.CreateLetterBoxPort;
import online.bottler.letter.application.port.out.CreateLetterKeywordPort;
import online.bottler.letter.application.port.out.CreateLetterPort;
import online.bottler.letter.application.port.out.DeleteLetterBoxPort;
import online.bottler.letter.application.port.out.DeleteLetterKeywordPort;
import online.bottler.letter.application.port.out.DeleteLetterPort;
import online.bottler.letter.application.port.out.LoadLetterKeywordPort;
import online.bottler.letter.application.port.out.LoadLetterPort;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.user.application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements CreateLetterWithKeywordsUseCase, GetLetterWithKeywordsDetailUseCase,
        DeleteLetterWithKeywordsUseCase {

    private final CreateLetterPort createLetterPort;
    private final CreateLetterKeywordPort createLetterKeywordPort;
    private final CreateLetterBoxPort createLetterBoxPort;
    private final CheckLetterBoxPort checkLetterBoxPort;
    private final CheckReplyLetterPort checkReplyLetterPort;
    private final LoadLetterKeywordPort loadLetterKeywordPort;
    private final LoadLetterPort loadLetterPort;
    private final DeleteLetterPort deleteLetterPort;
    private final DeleteLetterKeywordPort deleteLetterKeywordPort;
    private final DeleteLetterBoxPort deleteLetterBoxPort;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand command) {
        Letter letter = createLetterPort.create(command.toLetter());

        List<LetterKeyword> letterKeywords = LetterKeyword.createList(letter.getId(),
                command.toKeywords());
        createLetterKeywordPort.createAll(letterKeywords);

        createLetterBoxPort.createForLetter(letter.getId(), letter.getUserId(), letter.getCreatedAt());

        return LetterWithKeywordsResponse.from(LetterWithKeywords.create(letter, command.keywords()));
    }

    @Override
    @Transactional(readOnly = true)
    public LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery query) {
        if (!checkLetterBoxPort.existsByLetterIdAndUserId(query.letterId(), query.userId())) {
            throw new UnauthorizedLetterAccessException();
        }

        boolean isReplied = checkReplyLetterPort.existsByLetterIdAndUserId(query.letterId(), query.userId());
        List<LetterKeyword> keywords = loadLetterKeywordPort.loadKeywordsByLetterId(query.letterId());
        String profile = userRepository.findById(query.userId()).getImageUrl();
        Letter letter = loadLetterPort.loadById(query.letterId())
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));

        return LetterWithKeywordsDetailResponse.of(letter, keywords, query.userId(), profile, isReplied);
    }

    @Override
    @Transactional
    public void delete(LetterWithKeywordsDeleteCommand command) {
        deleteLetterPort.softDelete(command.letterId(), command.userId(), command.boxType());
        deleteLetterKeywordPort.softDelete(command.letterId());
        deleteLetterBoxPort.delete(command.letterId(), LetterType.LETTER, BoxType.NONE);
    }
}
