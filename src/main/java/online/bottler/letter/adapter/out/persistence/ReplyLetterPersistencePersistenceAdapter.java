package online.bottler.letter.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.ReplyLetterJpaRepository;
import online.bottler.letter.application.port.out.CheckReplyLetterPersistencePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterPersistencePersistenceAdapter implements CheckReplyLetterPersistencePort {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return replyLetterJpaRepository.existsByLetterIdAndSenderId(letterId, userId);
    }
}
