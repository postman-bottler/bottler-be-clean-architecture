package online.bottler.mapletter.application.port.out;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentReplyCachePort {
    List<Object> getRecentReplies(Long userId);

    void saveRecentReply(Long userId, String type, Long letterId, String labelUrl);

    void deleteRecentReply(Long userId, String type, Long letterId, String labelUrl);
}
