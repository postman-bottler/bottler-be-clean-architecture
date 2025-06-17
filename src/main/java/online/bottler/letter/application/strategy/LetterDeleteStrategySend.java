package online.bottler.letter.application.strategy;

import static online.bottler.letter.domain.BoxType.NONE;
import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LetterDeleteStrategySend implements LetterDeleteStrategy {
    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;

    @Override
    public void deleteLetters(List<Long> ids, Long userId) {
        letterPersistencePort.softDeleteByIds(ids);
        letterKeywordPersistencePort.softDeleteByIds(ids);
        letterBoxPersistencePort.deleteByCondition(ids, LETTER, NONE);
    }
}
