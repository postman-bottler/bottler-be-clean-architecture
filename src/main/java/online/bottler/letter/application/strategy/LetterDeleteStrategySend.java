package online.bottler.letter.application.strategy;

import static online.bottler.letter.domain.LetterType.LETTER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.in.LetterWithKeywordsUseCase;
import online.bottler.letter.domain.LetterBoxType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LetterDeleteStrategySend implements LetterDeleteStrategy {

    private final LetterBoxUseCase letterBoxUseCase;
    private final LetterWithKeywordsUseCase letterWithKeywordsUseCase;

    @Override
    public void deleteLetters(Long userId, List<Long> letterIds) {
        letterWithKeywordsUseCase.softDeleteByIds(letterIds);
        letterBoxUseCase.removeLettersFromBox(letterIds, LetterBoxType.of(LETTER, null));
    }
}
