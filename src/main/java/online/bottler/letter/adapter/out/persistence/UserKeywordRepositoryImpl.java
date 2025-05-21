package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJdbcRepository;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJpaRepository;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.port.out.UserKeywordRepository;
import online.bottler.letter.domain.UserKeyword;
import online.bottler.letter.adapter.out.persistence.entity.UserKeywordEntity;

@Repository
@RequiredArgsConstructor
public class UserKeywordRepositoryImpl implements UserKeywordRepository {

    private final UserKeywordJpaRepository jpaRepository;
    private final UserKeywordJdbcRepository jdbcRepository;

    @Override
    public List<UserKeyword> findUserKeywordsByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(UserKeywordEntity::toDomain).toList();
    }

    @Override
    public void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId) {
        jdbcRepository.deleteAllByUserId(userId);
        jdbcRepository.batchInsertKeywords(userKeywords);
    }

    @Override
    public List<String> findKeywordsByUserId(Long userId) {
        return jpaRepository.findKeywordsByUserId(userId);
    }
}
