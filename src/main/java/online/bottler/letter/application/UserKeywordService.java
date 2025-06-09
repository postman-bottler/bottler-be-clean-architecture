package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.application.port.in.UserKeywordUseCase;
import online.bottler.letter.application.port.out.UserKeywordPersistencePort;
import online.bottler.letter.application.response.UserKeywordResponse;
import online.bottler.letter.domain.UserKeyword;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserKeywordService implements UserKeywordUseCase {

    private final UserKeywordPersistencePort userKeywordPersistencePort;

    @Transactional
    @Override
    public void create(UserKeywordCommand command) {
        userKeywordPersistencePort.replaceKeywordsByUserId(command.toDomainList(), command.userId());
    }

    @Transactional(readOnly = true)
    @Override
    public UserKeywordResponse getKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordPersistencePort.loadUserKeywords(userId);
        return UserKeywordResponse.from(userKeywords);
    }
}
