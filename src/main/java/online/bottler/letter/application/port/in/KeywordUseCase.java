package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.domain.Keyword;

public interface KeywordUseCase {
    List<Keyword> getKeywords();
}
