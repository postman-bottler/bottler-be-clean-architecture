package online.bottler.letter.adapter.out.cache;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.cache.repository.RecommendationRedisRepository;
import online.bottler.letter.application.port.out.RecommendationCachePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecommendationCacheAdapter implements RecommendationCachePort {

    private final RecommendationRedisRepository recommendationRedisRepository;

    @Override
    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        recommendationRedisRepository.saveDeveloperLetter(userId, recommendations);
    }

    @Override
    public void saveTempRecommendations(Long userId, List<Long> recommendedLetters) {
        recommendationRedisRepository.saveTempRecommendations(userId, recommendedLetters);
    }

    @Override
    public List<Long> fetchTempRecommendations(Long userId) {
        return recommendationRedisRepository.fetchTempRecommendations(userId);
    }

    @Override
    public List<Long> fetchActiveRecommendations(Long userId) {
        return recommendationRedisRepository.fetchActiveRecommendations(userId);
    }

    @Override
    public void updateActiveRecommendations(Long userId, Long letterId) {
        recommendationRedisRepository.updateActiveRecommendations(userId, letterId);
    }
}
