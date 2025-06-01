package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.Keyword;

public interface KeywordPersistencePort {
    List<Keyword> getKeywords();
}
