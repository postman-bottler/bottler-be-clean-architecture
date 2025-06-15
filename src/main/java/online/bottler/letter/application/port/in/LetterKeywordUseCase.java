package online.bottler.letter.application.port.in;

import java.util.List;

public interface LetterKeywordUseCase {
    List<String> getTopFrequent(List<Long> letterIds, Long userId);
}
