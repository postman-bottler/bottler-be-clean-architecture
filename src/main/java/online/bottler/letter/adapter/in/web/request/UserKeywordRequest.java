package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import online.bottler.letter.application.command.UserKeywordCommand;

public record UserKeywordRequest(
        @NotNull(message = "키워드 목록은 필수입니다.") @NotEmpty(message = "키워드는 최소 하나 이상이어야 합니다.") List<String> keywords) {
    public UserKeywordCommand toCommand(Long userId) {
        return UserKeywordCommand.of(userId, keywords);
    }
}
