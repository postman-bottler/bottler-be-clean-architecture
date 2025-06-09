package online.bottler.letter.application.strategy;

import java.util.List;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public class LetterDeleteStrategySend implements LetterDeleteStrategy {
    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    public LetterDeleteStrategySend(LetterPersistencePort letterPersistencePort,
                                    LetterKeywordPersistencePort letterKeywordPersistencePort,
                                    LetterBoxPersistencePort letterBoxPersistencePort) {
        this.letterPersistencePort = letterPersistencePort;
        this.letterKeywordPersistencePort = letterKeywordPersistencePort;
        this.letterBoxPersistencePort = letterBoxPersistencePort;
    }

    @Override
    public void deleteLetters(List<Long> ids, Long userId) {
        letterPersistencePort.softDeleteByIds(ids);
        letterKeywordPersistencePort.softDeleteByIds(ids);
        letterBoxPersistencePort.deleteByCondition(ids, LetterType.LETTER, BoxType.NONE);
    }
}
