package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterKeywordJpaRepository;
import online.bottler.letter.adapter.out.persistence.repository.LetterKeywordQueryDslRepository;
import online.bottler.letter.application.port.out.CreateLetterKeywordPort;
import online.bottler.letter.application.port.out.DeleteLetterKeywordPort;
import online.bottler.letter.application.port.out.LoadLetterKeywordPort;
import online.bottler.letter.domain.LetterKeyword;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordPersistenceAdapter implements CreateLetterKeywordPort, LoadLetterKeywordPort,
        DeleteLetterKeywordPort {

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
