package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.RecommendedLetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.RecommendedLetterJpaRepository;
import online.bottler.letter.application.port.out.RecommendedLetterPersistencePort;
import online.bottler.letter.domain.RecommendedLetter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecommendedLetterPersistenceAdapter implements RecommendedLetterPersistencePort {

    private final RecommendedLetterJpaRepository recommendedLetterJpaRepository;

    @Override
    public void create(RecommendedLetter recommendedLetter) {
        recommendedLetterJpaRepository.save(RecommendedLetterEntity.from(recommendedLetter));
    }

    @Override
    public List<Long> findRecommendedLettersByUserId(Long userId) {
        return recommendedLetterJpaRepository.findRecommendedLetterIdsByUserId(userId);
    }
}
