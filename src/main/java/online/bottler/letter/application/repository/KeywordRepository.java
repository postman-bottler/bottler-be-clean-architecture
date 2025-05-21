package online.bottler.letter.application.repository;

import java.util.List;
import online.bottler.letter.domain.Keyword;

public interface KeywordRepository {

    List<Keyword> getKeywords();
}
