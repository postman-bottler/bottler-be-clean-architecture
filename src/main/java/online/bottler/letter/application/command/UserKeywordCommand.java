package online.bottler.letter.application.command;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public record UserKeywordCommand(Long userId, List<String> keywords) {
    public static UserKeywordCommand of(Long userId, List<String> keywords) {
        return new UserKeywordCommand(userId, keywords);
    }

    public UserKeyword toDomain(String keyword) {
        return UserKeyword.create(userId, keyword);
    }

    public List<UserKeyword> toDomainList() {
        return keywords.stream().map(this::toDomain).toList();
    }
}
