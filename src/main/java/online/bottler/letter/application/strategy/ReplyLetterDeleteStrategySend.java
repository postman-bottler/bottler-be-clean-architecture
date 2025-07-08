package online.bottler.letter.application.strategy;

import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.in.RecentReplyForLetterUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyLetterDeleteStrategySend implements LetterDeleteStrategy {

    private final RecentReplyForLetterUseCase recentReplyForLetterUseCase;
    private final LetterBoxUseCase letterBoxUseCase;
    private final ReplyLetterUseCase replyLetterUseCase;

    @Override
    public void deleteLetters(Long userId, List<Long> letterIds) {
        List<ReplyLetter> replyLetters = replyLetterUseCase.getReplyLettersByIdIn(letterIds);
        validateReplyLetterOwnerShip(userId, replyLetters);

        replyLetters.forEach(replyLetter -> recentReplyForLetterUseCase.delete(replyLetter.getReceiverId(),
                replyLetter.getId(), replyLetter.getLabel()));

        replyLetterUseCase.removeReplyLettersByIdIn(letterIds);
        letterBoxUseCase.removeLettersFromBox(letterIds, LetterBoxType.of(REPLY_LETTER, null));
    }

    private void validateReplyLetterOwnerShip(Long userId, List<ReplyLetter> replyLetters) {
        if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.isOwner(userId))) {
            throw new LetterAuthorMismatchException();
        }
    }
}
