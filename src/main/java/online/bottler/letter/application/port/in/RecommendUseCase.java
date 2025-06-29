package online.bottler.letter.application.port.in;

import java.util.List;
import java.util.Optional;

public interface RecommendUseCase {
    void saveDeveloperLetter(Long userId, List<Long> recommendations);

    String generate(Long userId);

    List<Long> getRecommended(Long userId);

    List<Long> fetchActiveRecommendations(Long userId);

    List<Long> fetchTempRecommendations(Long userId);

    Optional<Long> updateRecommendationsFromTemp(Long userId);
}
