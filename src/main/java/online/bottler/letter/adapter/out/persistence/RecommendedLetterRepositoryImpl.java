package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.RecommendedLetterJpaRepository;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.port.out.RecommendedLetterRepository;
import online.bottler.letter.domain.RecommendedLetter;
import online.bottler.letter.adapter.out.persistence.entity.RecommendedLetterEntity;

@Repository
@RequiredArgsConstructor
public class RecommendedLetterRepositoryImpl implements RecommendedLetterRepository {

    private final RecommendedLetterJpaRepository recommendedLetterJpaRepository;

    @Override
    public void saveRecommendedLetter(RecommendedLetter recommendedLetter) {
        recommendedLetterJpaRepository.save(RecommendedLetterEntity.from(recommendedLetter));
    }

    @Override
    public List<Long> findRecommendedLettersByUserId(Long userId) {
        return recommendedLetterJpaRepository.findRecommendedLetterIdsByUserId(userId);
    }
}
