package online.bottler.letter.application.port.in;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import org.springframework.transaction.annotation.Transactional;

public interface RecommendUseCase {
    void saveDeveloperLetter(Long userId, List<Long> recommendations);

    String generate(Long userId);

    @Transactional(readOnly = true)
    List<LetterRecommendSummaryResponse> getRecommended(Long userId);

    List<Long> fetchActiveRecommendations(Long userId);

    List<Long> fetchTempRecommendations(Long userId);

    Optional<Long> updateFromTemp(Long userId);
}
