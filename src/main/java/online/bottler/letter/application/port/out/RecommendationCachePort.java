package online.bottler.letter.application.port.out;

import java.util.List;

public interface RecommendationCachePort {
    void updateActiveRecommendations(Long userId, Long letterId);

    List<Long> fetchTempRecommendations(Long userId);

    void saveTempRecommendations(Long userId, List<Long> recommendedLetters);

    void saveDeveloperLetter(Long userId, List<Long> recommendations);

    List<Long> fetchActiveRecommendations(Long userId);
}
