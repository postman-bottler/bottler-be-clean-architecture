package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public interface UserKeywordPersistencePort {
    void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId);

    List<String> loadKeywords(Long userId);

    List<UserKeyword> loadUserKeywords(Long userId);
}
