package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.response.UserKeywordResponse;

public interface GetUserKeywordsUseCase {
    UserKeywordResponse getKeywords(Long userId);
}
