package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.KeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.KeywordJpaRepository;
import online.bottler.letter.application.port.out.KeywordPersistencePort;
import online.bottler.letter.domain.Keyword;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordPersistenceAdapter implements KeywordPersistencePort {

    private final KeywordJpaRepository keywordJpaRepository;

    @Override
    public List<Keyword> loadAll() {
        return KeywordEntity.toDomainList(keywordJpaRepository.findAll());
    }
}
