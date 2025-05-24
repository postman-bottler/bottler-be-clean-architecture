package online.bottler.reply.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.reply.application.port.in.ReplyUseCase;
import online.bottler.mapletter.application.ReplyFetchService;
import online.bottler.reply.application.response.ReplyResponse;

@Service
@RequiredArgsConstructor
public class ReplyService implements ReplyUseCase {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReplyFetchService replyFetchService;

    @Override
    @Transactional(readOnly = true)
    public List<ReplyResponse> findRecentReplyLetters(Long userId) {
        String key = "REPLY:" + userId;
        List<Object> values = redisTemplate.opsForList().range(key, 0, 2);

        if (values == null || values.size() < 3) {
            replyFetchService.fetchRecentReply(userId);
            values = redisTemplate.opsForList().range(key, 0, 2);
        }

        assert values != null;
        return values.stream()
                .map(value -> {
                    String[] parts = value.toString().split(":");
                    return ReplyResponse.from(ReplyType.valueOf(parts[0]), parts[2], Long.parseLong(parts[1]));
                })
                .collect(Collectors.toList());
    }
}
