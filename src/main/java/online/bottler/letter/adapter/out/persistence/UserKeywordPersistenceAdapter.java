package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.UserKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJpaRepository;
import online.bottler.letter.application.port.out.UserKeywordPersistencePort;
import online.bottler.letter.domain.UserKeyword;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserKeywordPersistenceAdapter implements UserKeywordPersistencePort {

    private final UserKeywordJpaRepository jpaRepository;

    @Override
    public void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId) {
        jpaRepository.deleteAllByUserId(userId);
        jpaRepository.saveAll(UserKeywordEntity.fromList(userKeywords));
    }

    @Override
    public List<String> loadKeywordsByUserId(Long userId) {
        return jpaRepository.findKeywordsByUserId(userId);
    }
}
