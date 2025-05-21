package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.RecommendedLetter;

public interface RecommendedLetterRepository {
    void saveRecommendedLetter(RecommendedLetter recommendedLetter);

    List<Long> findRecommendedLettersByUserId(Long userId);
}
