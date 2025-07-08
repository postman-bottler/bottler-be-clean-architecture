package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.UserKeywordCommand;

public interface UserKeywordUseCase {
    void create(UserKeywordCommand command);

    List<String> getKeywords(Long userId);
}
