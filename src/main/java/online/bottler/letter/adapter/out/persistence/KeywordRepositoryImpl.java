package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.KeywordJpaRepository;
import org.springframework.stereotype.Repository;
import online.bottler.letter.domain.Keyword;
import online.bottler.letter.adapter.out.persistence.entity.KeywordEntity;
import online.bottler.letter.application.port.out.KeywordRepository;

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
