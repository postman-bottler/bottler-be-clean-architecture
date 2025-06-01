package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.UserKeywordCommand;
import online.bottler.letter.application.response.UserKeywordResponse;

public interface UserKeywordUseCase {
    void create(UserKeywordCommand command);

    UserKeywordResponse getKeywords(Long userId);
}
