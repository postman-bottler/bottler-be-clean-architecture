package online.bottler.letter.v2.application;


import lombok.RequiredArgsConstructor;
import online.bottler.letter.v2.adapter.in.web.request.UserKeywordRequest;
import online.bottler.letter.v2.application.port.in.CreateUserKeywordsUseCase;
import online.bottler.letter.v2.application.port.in.GetUserKeywordsUseCase;
import online.bottler.letter.v2.application.response.UserKeywordResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserKeywordServiceV2 implements GetUserKeywordsUseCase, CreateUserKeywordsUseCase {

    @Override
    public void create(UserKeywordRequest request, Long userId) {

    }

    @Override
    public UserKeywordResponse getKeywords(Long userId) {
        return null;
    }
}
