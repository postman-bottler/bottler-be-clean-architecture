package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterKeywordJpaRepository;
import online.bottler.letter.application.port.out.CreateLetterKeywordPort;
import online.bottler.letter.domain.LetterKeyword;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordPersistenceAdapter implements CreateLetterKeywordPort {

    private final LetterKeywordJpaRepository jpaRepository;

    @Override
    public List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords) {
        return LetterKeywordEntity.toDomainList(jpaRepository.saveAll(LetterKeywordEntity.fromList(letterKeywords)));
    }
}
