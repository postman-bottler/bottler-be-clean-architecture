package online.bottler.mapletter.adaptor.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.adaptor.out.persistence.repository.ReplyLetterRedisRepository;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyRecentCacheAdaptor implements RecentReplyCachePort {

    private final ReplyLetterRedisRepository replyLetterRedisRepository;

    private static final int REDIS_SAVED_REPLY = 6;
    private static final String RECENT_REPLY_KEY_PREFIX = "REPLY:";

    @Override
    public List<Object> getRecentReplies(Long userId) {
        return replyLetterRedisRepository.getRecentReplies(createKey(userId));
    }

    @Override
    public void saveRecentReply(Long userId, String type, Long letterId, String labelUrl) {
        replyLetterRedisRepository.saveRecentReply(createKey(userId), createValue(type, letterId, labelUrl),
                REDIS_SAVED_REPLY);
    }

    @Override
    public void deleteRecentReply(Long userId, String type, Long letterId, String labelUrl) {
        replyLetterRedisRepository.deleteRecentReply(createKey(userId), createValue(type, letterId, labelUrl));
    }

    private String createKey(Long userId) {
        return RECENT_REPLY_KEY_PREFIX + userId;
    }

    private String createValue(String type, Long letterId, String labelUrl) {
        return type + ":" + letterId + ":" + labelUrl;
    }
}
