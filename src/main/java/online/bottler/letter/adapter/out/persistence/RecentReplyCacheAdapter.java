package online.bottler.letter.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.RecentReplyRedisRepository;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.PushRecentReplyCachePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecentReplyCacheAdapter implements PushRecentReplyCachePort, DeleteRecentReplyCachePort {
    private final RecentReplyRedisRepository recentReplyRedisRepository;

    @Override
    public void push(Long id, String label, Long receiverId) {
        recentReplyRedisRepository.push(id, label, receiverId);
    }

    @Override
    public void delete(Long receiverId, Long id, String label) {
        recentReplyRedisRepository.delete(receiverId, id, label);
    }
}
