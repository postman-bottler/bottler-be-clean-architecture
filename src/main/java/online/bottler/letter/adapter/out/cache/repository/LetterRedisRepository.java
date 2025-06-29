package online.bottler.letter.adapter.out.cache.repository;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.util.RedisLetterKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterRedisRepository {

    private final RedisTemplate<String, List<Long>> redisTemplate;

    public List<Long> fetchActiveByUserId(Long userId) {
        return fetchRecommendations(getActiveRecommendationKey(userId));
    }

    private List<Long> fetchRecommendations(String key) {
        List<Long> recommendations = redisTemplate.opsForValue().get(key);
        return hasRecommendations(recommendations) ? recommendations : new ArrayList<>();
    }

    private boolean hasRecommendations(List<Long> recommendations) {
        return !(recommendations == null || recommendations.isEmpty());
    }

    private String getActiveRecommendationKey(Long userId) {
        return RedisLetterKeyUtil.getActiveRecommendationKey(userId);
    }
}
