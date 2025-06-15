package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.ReplyLetterJpaRepository;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterPersistenceAdapter implements ReplyLetterPersistencePort {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return replyLetterJpaRepository.existsByLetterIdAndSenderId(letterId, userId);
    }

    @Override
    public ReplyLetter create(ReplyLetter replyLetter) {
        return replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter)).toDomain();
    }

    @Override
    public Page<ReplyLetter> loadSummariesByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable) {
        return replyLetterJpaRepository.findAllByLetterIdAndReceiverId(letterId, receiverId, pageable)
                .map(ReplyLetterEntity::toDomain);
    }

    @Override
    public Optional<ReplyLetter> loadById(Long id) {
        return replyLetterJpaRepository.findById(id).map(ReplyLetterEntity::toDomain);
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
    public void softBlock(Long id) {
        replyLetterJpaRepository.softBlockById(id);
    }

    @Override
    public void softDelete(Long id) {
        replyLetterJpaRepository.softDeleteById(id);
    }

    @Override
    public void softDeleteByIds(List<Long> ids) {
        replyLetterJpaRepository.softDeleteByIds(ids);
    }
}
