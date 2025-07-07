package online.bottler.letter.application.service;

import static online.bottler.letter.domain.LetterStatus.OPEN;
import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.port.in.BlockReplyLetterUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.DuplicateReplyLetterException;
import online.bottler.letter.exception.LetterNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyLetterService implements ReplyLetterUseCase, BlockReplyLetterUseCase {

    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final LetterPersistencePort letterPersistencePort;

    @Transactional
    @Override
    public ReplyLetter write(ReplyLetterCommand replyLetterCommand) {
        validateReplyLetterNotExists(replyLetterCommand.userId(), replyLetterCommand.letterId());

        Letter letter = getLetter(replyLetterCommand);

        return replyLetterPersistencePort.save(
                ReplyLetter.create(replyLetterCommand.userId(), letter.getUserId(), replyLetterCommand.letterId(),
                        replyLetterCommand.letterContent(), letter.getTitle()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ReplyLetter> getSummaries(ReplyLetterSummariesQuery replyLetterSummariesQuery) {
        return replyLetterPersistencePort.loadSummariesByLetterIdAndReceiverId(replyLetterSummariesQuery.letterId(),
                        replyLetterSummariesQuery.userId(), replyLetterSummariesQuery.commonPageCommand().toPageable());
    }

    @Transactional(readOnly = true)
    @Override
    public ReplyLetter get(Long id) {
        return findReplyLetter(id);
    }

    @Transactional
    @Override
    public ReplyLetter softDelete(ReplyLetterDeleteCommand replyLetterDeleteCommand) {
        ReplyLetter replyLetter = findReplyLetter(replyLetterDeleteCommand.id());
        replyLetter.delete();
        replyLetterPersistencePort.save(replyLetter);

        return replyLetter;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isReplied(Long userId, Long letterId) {
        return hasReplyLetter(userId, letterId);
    }


    @Transactional(readOnly = true)
    @Override
    public List<Long> getIdsByUserId(Long userId) {
        return replyLetterPersistencePort.loadIdsByUserId(userId);
    }

    @Override
    public void softDeleteByIds(List<Long> ids) {
        List<ReplyLetter> replyLetters = replyLetterPersistencePort.loadAllByIdInAndStatus(ids, OPEN);
        replyLetters.forEach(ReplyLetter::delete);
        replyLetterPersistencePort.createAll(replyLetters);
    }

    @Override
    public List<ReplyLetter> getAllByIds(List<Long> ids) {
        return replyLetterPersistencePort.loadAllByIds(ids);
    }

    @Transactional
    @Override
    public Long softBlock(Long id) {
        ReplyLetter replyLetter = findReplyLetter(id);
        replyLetter.block();
        replyLetterPersistencePort.save(replyLetter);

        return replyLetter.getSenderId();
    }

    private void validateReplyLetterNotExists(Long userId, Long letterId) {
        if (hasReplyLetter(userId, letterId)) {
            throw new DuplicateReplyLetterException();
        }
    }

    private boolean hasReplyLetter(Long userId, Long letterId) {
        return replyLetterPersistencePort.existsByUserIdAndLetterId(userId, letterId);
    }

    private Letter getLetter(ReplyLetterCommand replyLetterCommand) {
        return letterPersistencePort.loadByIdAndStatus(replyLetterCommand.letterId(), LetterStatus.OPEN)
                .orElseThrow(LetterNotFoundException::new);
    }

    private ReplyLetter findReplyLetter(Long id) {
        return replyLetterPersistencePort.loadById(id)
                .orElseThrow(() -> new LetterNotFoundException(REPLY_LETTER));
    }
}
