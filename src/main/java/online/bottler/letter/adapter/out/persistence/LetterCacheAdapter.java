package online.bottler.letter.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.port.out.LoadRecommendedLetterIdsCachePort;
import online.bottler.letter.utiil.RedisLetterKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LetterCacheAdapter implements LoadRecommendedLetterIdsCachePort {

    private final RedisTemplate<String, List<Long>> redisTemplate;

    @Override
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
