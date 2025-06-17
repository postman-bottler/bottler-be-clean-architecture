package online.bottler.letter.application.service;

import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.port.in.BlockReplyLetterUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.LetterNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyLetterService implements ReplyLetterUseCase, BlockReplyLetterUseCase {

    private final ReplyLetterPersistencePort replyLetterPersistencePort;

    @Transactional
    @Override
    public ReplyLetter create(ReplyLetterCommand replyLetterCommand, Long userId, String title) {
        return replyLetterPersistencePort.create(
                ReplyLetter.create(replyLetterCommand.userId(), replyLetterCommand.letterContent(),
                        replyLetterCommand.letterId(), userId, title));
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
        replyLetterPersistencePort.softDelete(replyLetterDeleteCommand.id());
        return findReplyLetter(replyLetterDeleteCommand.id());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isReplied(Long letterId, Long userId) {
        return replyLetterPersistencePort.existsByLetterIdAndUserId(letterId, userId);
    }

    @Transactional
    @Override
    public Long softBlock(Long id) {
        replyLetterPersistencePort.softBlock(id);
        return findReplyLetter(id).getUserId();
    }

    private ReplyLetter findReplyLetter(Long id) {
        return replyLetterPersistencePort.loadById(id)
                .orElseThrow(() -> new LetterNotFoundException(REPLY_LETTER));
    }
}
