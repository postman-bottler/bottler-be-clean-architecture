package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterKeywordJpaRepository;
import online.bottler.letter.adapter.out.persistence.repository.LetterKeywordQueryDslRepository;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordPersistenceAdapter implements LetterKeywordPersistencePort {

    private final LetterKeywordQueryDslRepository queryDslRepository;
    private final LetterKeywordJpaRepository letterKeywordJpaRepository;

    @Override
    public void saveAll(List<LetterKeyword> letterKeywords) {
        letterKeywordJpaRepository.saveAll(LetterKeywordEntity.fromList(letterKeywords));
    }

    @Override
    public List<LetterKeyword> loadAllByLetterId(Long letterId) {
        return LetterKeywordEntity.toDomainList(queryDslRepository.findKeywordsByLetterId(letterId));
    }

    @Override
    public List<LetterKeyword> loadAllByLetterIdInAndStatus(List<Long> letterIds, LetterStatus status) {
        return LetterKeywordEntity.toDomainList(letterKeywordJpaRepository.findAllByLetterIdInAndStatus(letterIds, status));
    }

    @Override
    public List<String> loadKeywordsByLetterIdAndStatus(Long letterId, LetterStatus status) {
        return letterKeywordJpaRepository.findKeywordsByLetterIdAndStatus(letterId, status);
    }

    @Override
    public List<String> loadFrequentKeywords(List<Long> letterIds) {
        return queryDslRepository.getFrequentKeywords(letterIds);
    }

    @Override
    public List<Long> loadMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        return queryDslRepository.getMatchedLetters(userKeywords, letterIds, limit);
    }
}
