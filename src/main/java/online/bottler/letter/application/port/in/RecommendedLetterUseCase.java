package online.bottler.letter.application.port.in;

import java.util.List;

public interface RecommendedLetterUseCase {
    List<Long> findRecommendedLetterIdsByUserId(Long userId);
}
