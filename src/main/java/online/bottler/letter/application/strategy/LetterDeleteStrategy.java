package online.bottler.letter.application.strategy;

import java.util.List;

public interface LetterDeleteStrategy {
    void deleteLetters(Long userId, List<Long> letterIds);
}
