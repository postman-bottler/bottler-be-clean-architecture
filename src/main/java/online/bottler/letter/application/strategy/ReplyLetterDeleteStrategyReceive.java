package online.bottler.letter.application.strategy;

import java.util.List;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public class ReplyLetterDeleteStrategyReceive implements LetterDeleteStrategy {
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    public ReplyLetterDeleteStrategyReceive(LetterBoxPersistencePort letterBoxPersistencePort) {
        this.letterBoxPersistencePort = letterBoxPersistencePort;
    }

    @Override
    public void deleteLetters(List<Long> ids, Long userId) {
        letterBoxPersistencePort.deleteByConditionAndUserId(ids, LetterType.LETTER, BoxType.RECEIVE, userId);
    }
}
