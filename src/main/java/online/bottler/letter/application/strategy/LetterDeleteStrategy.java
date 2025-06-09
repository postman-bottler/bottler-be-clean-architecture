package online.bottler.letter.application.strategy;

import java.util.List;

public interface LetterDeleteStrategy {
    void deleteLetters(List<Long> ids, Long userId);
}
