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
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.DuplicateReplyLetterException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyLetterService implements ReplyLetterUseCase, BlockReplyLetterUseCase {

    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    @Override
    @Transactional
    public ReplyLetter write(ReplyLetterCommand replyLetterCommand) {
        validateReplyLetterNotExists(replyLetterCommand.userId(), replyLetterCommand.letterId());

        Letter letter = getLetter(replyLetterCommand);

        return replyLetterPersistencePort.save(
                ReplyLetter.create(
                        replyLetterCommand.userId(),
                        letter.getUserId(),
                        replyLetterCommand.letterId(),
                        replyLetterCommand.letterContent(),
                        letter.getTitle()
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyLetter getReplyLetter(Long userId, Long id) {
        validateUserAccess(userId, id);

        return getReplyLetter(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplyLetter> getReplyLetters(List<Long> ids) {
        return replyLetterPersistencePort.loadAllByIdInAndStatus(ids, OPEN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getReplyLetterIds(Long userId) {
        return replyLetterPersistencePort.loadIdsByUserIdAndStatus(userId, OPEN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReplyLetter> getPagedReplyLetters(ReplyLetterSummariesQuery replyLetterSummariesQuery) {
        validateUserAccess(replyLetterSummariesQuery.userId(), replyLetterSummariesQuery.letterId());

        return replyLetterPersistencePort.loadAllByReceiverIdAndLetterIdAndStatus(
                replyLetterSummariesQuery.userId(),
                replyLetterSummariesQuery.letterId(),
                OPEN,
                replyLetterSummariesQuery.commonPageCommand().toPageable()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isReplied(Long userId, Long letterId) {
        return hasReplyLetter(userId, letterId);
    }

    @Override
    @Transactional
    public ReplyLetter removeReplyLetter(ReplyLetterDeleteCommand replyLetterDeleteCommand) {
        ReplyLetter replyLetter = getReplyLetter(replyLetterDeleteCommand.id());
        replyLetter.delete();
        replyLetterPersistencePort.save(replyLetter);

        return replyLetter;
    }

    @Override
    @Transactional
    public void removeReplyLetters(List<Long> ids) {
        List<ReplyLetter> replyLetters = replyLetterPersistencePort.loadAllByIdInAndStatus(ids, OPEN);
        replyLetters.forEach(ReplyLetter::delete);
        replyLetterPersistencePort.saveAll(replyLetters);
    }

    @Override
    @Transactional
    public Long blockReplyLetter(Long id) {
        ReplyLetter replyLetter = getReplyLetter(id);
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
        return replyLetterPersistencePort.existsBySenderIdAndLetterId(userId, letterId);
    }

    private Letter getLetter(ReplyLetterCommand replyLetterCommand) {
        return letterPersistencePort.loadByIdAndStatus(replyLetterCommand.letterId(), LetterStatus.OPEN)
                .orElseThrow(LetterNotFoundException::new);
    }

    private void validateUserAccess(Long userId, Long letterId) {
        if (!isLetterInBox(userId, letterId)) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private boolean isLetterInBox(Long userId, Long letterId) {
        return letterBoxPersistencePort.existsByUserIdAndLetterId(userId, letterId);
    }

    private ReplyLetter getReplyLetter(Long id) {
        return replyLetterPersistencePort.loadByIdAndStatus(id, OPEN)
                .orElseThrow(() -> new LetterNotFoundException(REPLY_LETTER));
    }
}
