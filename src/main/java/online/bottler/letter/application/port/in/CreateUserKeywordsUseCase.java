package online.bottler.letter.application.port.in;

import online.bottler.letter.adapter.in.web.request.UserKeywordRequest;

public interface CreateUserKeywordsUseCase {
    void create(UserKeywordRequest request, Long userId);
}
