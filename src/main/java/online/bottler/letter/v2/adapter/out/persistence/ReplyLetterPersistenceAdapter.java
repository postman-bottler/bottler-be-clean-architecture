package online.bottler.letter.v2.adapter.out.persistence;


import lombok.RequiredArgsConstructor;
import online.bottler.letter.infra.ReplyLetterJpaRepository;
import online.bottler.letter.v2.application.port.out.CheckReplyLetterPersistencePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterPersistenceAdapter implements CheckReplyLetterPersistencePort {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return replyLetterJpaRepository.existsByLetterIdAndSenderId(letterId, userId);
    }
}
