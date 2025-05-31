package online.bottler.letter.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.util.RedisLetterKeyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecommendationRedisRepository {

    @Value("${recommendation.limit.active-recommendations}")
    private int maxRecommendations;

    private final RedisTemplate<String, List<Long>> redisTemplate;

    public void saveTempRecommendations(Long userId, List<Long> recommendations) {
        redisTemplate.opsForValue().set(getTempRecommendationKey(userId), recommendations);
    }

    public void updateActiveRecommendations(Long userId, Long letterId) {
        List<Long> activeRecommendations = fetchActiveRecommendations(userId);

        if (activeRecommendations.size() - maxRecommendations >= 0) {
            activeRecommendations.subList(0, activeRecommendations.size() - maxRecommendations).clear();
        }
        activeRecommendations.add(letterId);

        redisTemplate.opsForValue().set(getActiveRecommendationKey(userId), activeRecommendations);
        redisTemplate.delete(getTempRecommendationKey(userId));
    }

    public List<Long> fetchActiveRecommendations(Long userId) {
        return fetchRecommendations(getActiveRecommendationKey(userId));
    }

    public List<Long> fetchTempRecommendations(Long userId) {
        return fetchRecommendations(getTempRecommendationKey(userId));
    }

    private List<Long> fetchRecommendations(String key) {
        List<Long> recommendations = redisTemplate.opsForValue().get(key);
        if (isExistRecommendations(recommendations)) {
            return recommendations;
        } else {
            return new ArrayList<>();
        }
    }

    private boolean isExistRecommendations(List<Long> recommendations) {
        return !(recommendations == null || recommendations.isEmpty());
    }

    private String getTempRecommendationKey(Long userId) {
        return RedisLetterKeyUtil.getTempRecommendationKey(userId);
    }

    private String getActiveRecommendationKey(Long userId) {
        return RedisLetterKeyUtil.getActiveRecommendationKey(userId);
    }

    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        redisTemplate.opsForValue().set(getActiveRecommendationKey(userId), recommendations);
    }
}
