package online.bottler.letter.application.strategy;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.domain.LetterBoxType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyLetterDeleteStrategyReceive implements LetterDeleteStrategy {
    private final LetterBoxUseCase letterBoxUseCase;

    @Override
    public void deleteLetters(List<Long> letterIds, Long userId) {
        letterBoxUseCase.removeLettersFromBox(userId, letterIds, LetterBoxType.of(LETTER, RECEIVE));
    }
}
