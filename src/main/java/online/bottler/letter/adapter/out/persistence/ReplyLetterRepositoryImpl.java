package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.repository.ReplyLetterJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.port.out.ReplyLetterRepository;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRepositoryImpl implements ReplyLetterRepository {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public ReplyLetter save(ReplyLetter replyLetter) {
        return replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter)).toDomain();
    }

    @Override
    public Page<ReplyLetter> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable) {
        return replyLetterJpaRepository.findAllByLetterIdAndReceiverId(letterId, receiverId, pageable)
                .map(ReplyLetterEntity::toDomain);
    }

    @Override
    public Optional<ReplyLetter> findById(Long replyLetterId) {
        return replyLetterJpaRepository.findById(replyLetterId).map(ReplyLetterEntity::toDomain);
    }

    @Override
    public List<ReplyLetter> findAllByIds(List<Long> letterIds) {
        return replyLetterJpaRepository.findAllByIds(letterIds).stream()
                .map(ReplyLetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<ReplyLetter> findAllBySenderId(Long senderId) {
        return replyLetterJpaRepository.findAllBySenderId(senderId).stream()
                .map(ReplyLetterEntity::toDomain)
                .toList();
    }

    @Override
    public void softDeleteByIds(List<Long> letterIds) {
        replyLetterJpaRepository.softDeleteByIds(letterIds);
    }

    @Override
    public void softBlockById(Long replyLetterId) {
        replyLetterJpaRepository.softBlockById(replyLetterId);
    }

    @Override
    public boolean existsByLetterIdAndSenderId(Long letterId, Long senderId) {
        return replyLetterJpaRepository.existsByLetterIdAndSenderId(letterId, senderId);
    }

    @Override
    public boolean existsByIdAndSenderId(Long replyLetterId, Long userId) {
        return replyLetterJpaRepository.existsByIdAndSenderId(replyLetterId, userId);
    }
}
