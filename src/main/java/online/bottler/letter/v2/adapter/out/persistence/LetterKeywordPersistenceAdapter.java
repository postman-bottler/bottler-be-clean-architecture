package online.bottler.letter.v2.adapter.out.persistence;


import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.infra.LetterKeywordJpaRepository;
import online.bottler.letter.infra.LetterKeywordQueryDslRepository;
import online.bottler.letter.infra.entity.LetterKeywordEntity;
import online.bottler.letter.v2.application.port.out.CreateLetterKeywordPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterKeywordPersistencePort;
import online.bottler.letter.v2.application.port.out.LoadLetterKeywordPersistencePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordPersistenceAdapter implements CreateLetterKeywordPersistencePort,
        LoadLetterKeywordPersistencePort,
        DeleteLetterKeywordPersistencePort {

    private final LetterKeywordQueryDslRepository queryDslRepository;
    private final LetterKeywordJpaRepository jpaRepository;

    @Override
    public List<LetterKeyword> createAll(List<LetterKeyword> letterKeywords) {
        return LetterKeywordEntity.toDomainList(jpaRepository.saveAll(LetterKeywordEntity.fromList(letterKeywords)));
    }

    @Override
    public List<LetterKeyword> loadKeywordsByLetterId(Long letterId) {
        return LetterKeywordEntity.toDomainList(queryDslRepository.findKeywordsByLetterId(letterId));
    }

    @Override
    public void softDelete(Long letterId) {
        jpaRepository.softDeleteById(letterId);
    }
}
