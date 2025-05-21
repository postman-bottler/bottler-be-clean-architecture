package online.bottler.letter.application.repository;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public interface UserKeywordRepository {
    List<UserKeyword> findUserKeywordsByUserId(Long userId);

    void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId);

    List<String> findKeywordsByUserId(Long userId);
}
