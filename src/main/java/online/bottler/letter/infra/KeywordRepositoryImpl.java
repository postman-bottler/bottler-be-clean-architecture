package online.bottler.letter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.letter.domain.Keyword;
import online.bottler.letter.infra.entity.KeywordEntity;
import online.bottler.letter.application.repository.KeywordRepository;

@Repository
@RequiredArgsConstructor
public class KeywordRepositoryImpl implements KeywordRepository {

    private final KeywordJpaRepository keywordJpaRepository;

    @Override
    public List<Keyword> getKeywords() {
        List<KeywordEntity> keywords = keywordJpaRepository.findAll();
        return keywords.stream().map(KeywordEntity::toDomain).toList();
    }
}
