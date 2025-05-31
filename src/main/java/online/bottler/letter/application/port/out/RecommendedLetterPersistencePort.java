package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.RecommendedLetter;

public interface RecommendedLetterPersistencePort {
    void create(RecommendedLetter recommendedLetter);

    List<Long> findRecommendedLettersByUserId(Long userId);
}
