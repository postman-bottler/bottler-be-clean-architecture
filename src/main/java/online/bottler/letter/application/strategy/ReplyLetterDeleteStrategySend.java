package online.bottler.letter.application.strategy;

import java.util.List;
import online.bottler.letter.application.port.out.DeleteRecentReplyCachePort;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.ReplyLetterPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.LetterAuthorMismatchException;

public class ReplyLetterDeleteStrategySend implements LetterDeleteStrategy {

    private final ReplyLetterPersistencePort replyLetterPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final DeleteRecentReplyCachePort deleteRecentReplyCachePort;

    public ReplyLetterDeleteStrategySend(ReplyLetterPersistencePort replyLetterPersistencePort,
                                         LetterBoxPersistencePort letterBoxPersistencePort,
                                         DeleteRecentReplyCachePort deleteRecentReplyCachePort) {
        this.replyLetterPersistencePort = replyLetterPersistencePort;
        this.letterBoxPersistencePort = letterBoxPersistencePort;
        this.deleteRecentReplyCachePort = deleteRecentReplyCachePort;
    }

    @Override
    public void deleteLetters(List<Long> ids, Long userId) {
        List<ReplyLetter> replyLetters = replyLetterPersistencePort.loadAllByIds(ids);
        validateReplyLetterOwnerShip(userId, replyLetters);

        replyLetters.forEach(replyLetter -> deleteRecentReplyCachePort.delete(replyLetter.getReceiverId(),
                replyLetter.getId(), replyLetter.getLabel()));

        replyLetterPersistencePort.softDeleteByIds(ids);
        letterBoxPersistencePort.deleteByCondition(ids, LetterType.REPLY_LETTER, BoxType.NONE);
    }

    private void validateReplyLetterOwnerShip(Long userId, List<ReplyLetter> replyLetters) {
        if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.getUserId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }
    }
}
