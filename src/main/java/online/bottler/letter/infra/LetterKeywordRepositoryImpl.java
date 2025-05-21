package online.bottler.letter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.repository.LetterKeywordRepository;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.infra.entity.LetterKeywordEntity;

@Repository
@RequiredArgsConstructor
public class LetterKeywordRepositoryImpl implements LetterKeywordRepository {

    private final LetterKeywordQueryDslRepository queryDslRepository;
    private final LetterKeywordJpaRepository jpaRepository;

    @Override
    public List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords) {
        return jpaRepository.saveAll(letterKeywords.stream().map(LetterKeywordEntity::from).toList())
                .stream()
                .map(LetterKeywordEntity::toDomain)
                .toList();
    }

    @Override
    public List<LetterKeyword> getKeywordsByLetterId(Long letterId) {
        return queryDslRepository.findKeywordsByLetterId(letterId).stream().map(LetterKeywordEntity::toDomain).toList();
    }

    @Override
    public List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        return queryDslRepository.getMatchedLetters(userKeywords, letterIds, limit);
    }

    @Override
    public void markKeywordsAsDeleted(List<Long> letterIds) {
        jpaRepository.updateIsDeleted(letterIds);
    }

    @Override
    public List<String> getFrequentKeywords(List<Long> letterIds) {
        return queryDslRepository.getFrequentKeywords(letterIds);
    }
}
