package online.bottler.letter.application.facade;

import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.RecommendUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterWithKeywords;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LetterWithKeywordsFacade {

    private final LetterWithKeywordsUseCase letterWithKeywordsUseCase;
    private final ReplyLetterUseCase replyLetterUseCase;
    private final LetterBoxUseCase letterBoxUseCase;
    private final RecommendUseCase recommendUseCase;
    private final UserUseCase userUseCase;

    @Transactional
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = letterWithKeywordsUseCase.write(letterWithKeywordsCommand);

        letterBoxUseCase.archiveLetter(letter);

        return LetterWithKeywordsResponse.from(LetterWithKeywords.create(letter, letterWithKeywordsCommand.keywords()));
    }

    @Transactional(readOnly = true)
    public LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery) {
        LetterWithKeywords letterWithKeywords = letterWithKeywordsUseCase.getLetterWithKeywords(letterWithKeywordsDetailQuery);

        String profile = userUseCase.findById(letterWithKeywords.getUserId()).getImageUrl();

        boolean isOwner = letterWithKeywords.isOwner(letterWithKeywordsDetailQuery.userId());

        boolean isReplied = replyLetterUseCase.isReplied(letterWithKeywordsDetailQuery.userId(),
                letterWithKeywordsDetailQuery.letterId());

        return LetterWithKeywordsDetailResponse.of(letterWithKeywords, profile, isOwner, isReplied);
    }

    @Transactional(readOnly = true)
    public List<LetterRecommendSummaryResponse> getRecommended(Long userId) {
        List<Long> recommendedLetterIds = recommendUseCase.getRecommended(userId);

        List<Letter> letters = letterWithKeywordsUseCase.getLettersIncludingAllStatus(recommendedLetterIds);

        return LetterRecommendSummaryResponse.fromList(letters);
    }

    @Transactional
    public void delete(LetterWithKeywordsDeleteCommand letterWithKeywordsDeleteCommand) {
        letterWithKeywordsUseCase.deleteLetter(letterWithKeywordsDeleteCommand.userId(), letterWithKeywordsDeleteCommand.letterId());

        letterBoxUseCase.removeLetterFromBox(letterWithKeywordsDeleteCommand.letterId(),
                LetterBoxType.of(LETTER, null));
    }
}
