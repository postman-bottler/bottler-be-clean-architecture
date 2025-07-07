package online.bottler.letter.application.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.application.port.in.KeywordUseCase;
import online.bottler.letter.application.port.in.LetterKeywordUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.UserKeywordUseCase;
import online.bottler.letter.application.response.FrequentKeywordsResponse;
import online.bottler.letter.application.response.KeywordResponse;
import online.bottler.letter.application.response.UserKeywordResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KeywordFacade {

    private final KeywordUseCase keywordUseCase;
    private final UserKeywordUseCase userKeywordUseCase;
    private final LetterKeywordUseCase letterKeywordUseCase;
    private final LetterWithKeywordsUseCase letterWithKeywordsUseCase;

    @Transactional
    public void createUserKeywords(UserKeywordCommand userKeywordCommand) {
        userKeywordUseCase.create(userKeywordCommand);
    }

    @Transactional(readOnly = true)
    public KeywordResponse getKeywordList() {
        return KeywordResponse.from(keywordUseCase.getAll());
    }

    @Transactional(readOnly = true)
    public UserKeywordResponse getUserKeywords(Long userId) {
        return UserKeywordResponse.from(userKeywordUseCase.getKeywords(userId));
    }

    @Transactional(readOnly = true)
    public FrequentKeywordsResponse getTopFrequentKeywords(Long userId) {
        List<Long> letterIds = letterWithKeywordsUseCase.getLetterIdsByUserId(userId);

        return FrequentKeywordsResponse.from(letterKeywordUseCase.getTopFrequent(letterIds, userId));
    }
}
