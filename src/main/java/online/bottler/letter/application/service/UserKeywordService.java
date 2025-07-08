package online.bottler.letter.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.application.port.in.UserKeywordUseCase;
import online.bottler.letter.application.port.out.UserKeywordPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserKeywordService implements UserKeywordUseCase {

    private final UserKeywordPersistencePort userKeywordPersistencePort;

    @Override
    @Transactional
    public void create(UserKeywordCommand command) {
        userKeywordPersistencePort.replaceKeywordsByUserId(command.toDomainList(), command.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getKeywords(Long userId) {
        return userKeywordPersistencePort.loadKeywordsByUserId(userId);
    }
}
