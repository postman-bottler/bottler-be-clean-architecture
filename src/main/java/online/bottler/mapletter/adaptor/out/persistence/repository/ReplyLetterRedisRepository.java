package online.bottler.mapletter.adaptor.out.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public List<Object> getRecentReplies(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void saveRecentReply(String key, String value, int redisSavedReplySize) {
        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= redisSavedReplySize) {
            redisTemplate.opsForList().rightPop(key);
        }

        if (!redisTemplate.opsForList().range(key, 0, -1).contains(value)) {
            redisTemplate.opsForList().leftPush(key, value);
        }
    }

    public void deleteRecentReply(String key, String value) {
        redisTemplate.opsForList().remove(key, 1, value);
    }
}
