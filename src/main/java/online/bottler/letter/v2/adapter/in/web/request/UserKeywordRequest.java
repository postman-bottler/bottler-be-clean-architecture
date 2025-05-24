package online.bottler.letter.v2.adapter.in.web.request;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public record UserKeywordRequest(List<String> keywords) {
    public List<UserKeyword> toDomain(Long userId) {
        return keywords.stream()
                .map(keyword ->
                        UserKeyword.create(userId, keyword))
                .toList();
    }
}
