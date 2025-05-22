package online.bottler.mapletter.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.port.in.ReplyFetchUseCase;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import org.springframework.stereotype.Service;
import online.bottler.mapletter.application.dto.ReplyProjectDTO;
import online.bottler.mapletter.application.port.out.ReplyMapLetterPersistencePort;

@Service
@RequiredArgsConstructor
public class ReplyFetchService implements ReplyFetchUseCase {
    private final RecentReplyCachePort recentReplyCachePort;
    private final ReplyMapLetterPersistencePort replyMapLetterPersistencePort;

    private static final int REDIS_SAVED_REPLY = 6;

    public void fetchRecentReply(Long userId) {
        List<Object> redisValues = recentReplyCachePort.getRecentReplies(userId);
        int fetchItemSize = REDIS_SAVED_REPLY - (redisValues == null ? 0 : redisValues.size());

        // Redis에 저장된 값이 부족하면 DB에서 조회 후 Redis에 저장
        if (fetchItemSize > 0) {
            List<ReplyProjectDTO> recentMapKeywordReplyByUserId =
                    replyMapLetterPersistencePort.findRecentMapKeywordReplyByUserId(userId, fetchItemSize);

            List<ReplyProjectDTO> reversedList = new ArrayList<>(recentMapKeywordReplyByUserId);
            Collections.reverse(reversedList);

            List<Object> existingValues = recentReplyCachePort.getRecentReplies(userId);

            for (ReplyProjectDTO reply : reversedList) {
                String tempValue = reply.getType() + ":" + reply.getId() + ":" + reply.getLabel();

                if (existingValues == null || !existingValues.contains(tempValue)) {
                    recentReplyCachePort.saveRecentReply(userId, reply.getType(), reply.getId(),
                            reply.getLabel());
                }
            }
        }
    }
}
