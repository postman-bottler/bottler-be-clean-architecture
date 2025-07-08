package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.ReplyLetterJpaRepository;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterPersistenceAdapter implements ReplyLetterPersistencePort {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public boolean existsBySenderIdAndLetterId(Long userId, Long letterId) {
        return replyLetterJpaRepository.existsBySenderIdAndLetterId(userId, letterId);
    }

    @Override
    public ReplyLetter save(ReplyLetter replyLetter) {
        return replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter)).toDomain();
    }

    @Override
    public Page<ReplyLetter> loadAllByReceiverIdAndLetterIdAndStatus(Long receiverId, Long letterId, LetterStatus status, Pageable pageable) {
        return replyLetterJpaRepository.findAllByReceiverIdAndLetterIdAndStatus(receiverId, letterId, status, pageable)
                .map(ReplyLetterEntity::toDomain);
    }

    @Override
    public Optional<ReplyLetter> loadByIdAndStatus(Long id, LetterStatus status) {
        return replyLetterJpaRepository.findByIdAndStatus(id, status).map(ReplyLetterEntity::toDomain);
    }

    @Override
    public List<Long> loadIdsByUserId(Long userId) {
        return replyLetterJpaRepository.findIdsBySenderId(userId);
    }

    @Override
    public List<ReplyLetter> loadAllByIds(List<Long> ids) {
        return ReplyLetterEntity.toDomainList(replyLetterJpaRepository.findAllByIds(ids));
    }

    @Override
    public void saveAll(List<ReplyLetter> replyLetters) {
        replyLetterJpaRepository.saveAll(ReplyLetterEntity.fromList(replyLetters));
    }

    @Override
    public List<ReplyLetter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus status) {
        return ReplyLetterEntity.toDomainList(replyLetterJpaRepository.findAllByIdInAndStatus(ids, status));
    }
}
