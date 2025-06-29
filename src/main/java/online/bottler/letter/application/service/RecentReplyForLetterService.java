package online.bottler.letter.application.service;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.RecentReplyForLetterUseCase;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.PushRecentReplyCachePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecentReplyForLetterService implements RecentReplyForLetterUseCase {

    private final PushRecentReplyCachePort pushRecentReplyCachePort;
    private final DeleteRecentReplyCachePort deleteRecentReplyCachePort;

    @Override
    public void push(Long id, String label, Long receiverId) {
        pushRecentReplyCachePort.push(id, label, receiverId);
    }

    @Override
    public void delete(Long receiverId, Long id, String label) {
        deleteRecentReplyCachePort.delete(receiverId, id, label);
    }
}
