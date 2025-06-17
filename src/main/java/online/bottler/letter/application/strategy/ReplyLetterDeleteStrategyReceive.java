package online.bottler.letter.application.strategy;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyLetterDeleteStrategyReceive implements LetterDeleteStrategy {
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    @Override
    public void deleteLetters(List<Long> ids, Long userId) {
        letterBoxPersistencePort.deleteByConditionAndUserId(ids, LETTER, RECEIVE, userId);
    }
}
