package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.UserKeywordResponse;

public interface GetUserKeywordsUseCase {
    UserKeywordResponse getKeywords(Long userId);
}
