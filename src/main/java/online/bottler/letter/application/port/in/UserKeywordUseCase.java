package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.domain.UserKeyword;

public interface UserKeywordUseCase {
    void create(UserKeywordCommand command);

    List<UserKeyword> getKeywords(Long userId);
}
