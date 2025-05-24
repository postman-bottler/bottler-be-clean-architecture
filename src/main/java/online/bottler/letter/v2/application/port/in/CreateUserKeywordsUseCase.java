package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.adapter.in.web.request.UserKeywordRequest;

public interface CreateUserKeywordsUseCase {
    void create(UserKeywordRequest request, Long userId);
}
