package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.application.port.in.UserKeywordUseCase;
import online.bottler.letter.application.port.out.UserKeywordPersistencePort;
import online.bottler.letter.application.response.UserKeywordResponse;
import online.bottler.letter.domain.UserKeyword;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserKeywordServiceV2 implements UserKeywordUseCase {

    private final UserKeywordPersistencePort userKeywordPersistencePort;

    @Override
    public void create(UserKeywordCommand command) {
        userKeywordPersistencePort.replaceKeywordsByUserId(command.toDomainList(), command.userId());
    }

    @Override
    public UserKeywordResponse getKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordPersistencePort.loadUserKeywords(userId);
        return UserKeywordResponse.from(userKeywords);
    }
}
