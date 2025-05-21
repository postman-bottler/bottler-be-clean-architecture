package online.bottler.letter.adapter.in.web.dto.request;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public record UserKeywordRequestDTO(List<String> keywords) {
    public List<UserKeyword> toDomain(Long userId) {
        return keywords.stream()
                .map(keyword ->
                        UserKeyword.builder().userId(userId).keyword(keyword).build())
                .toList();
    }
}
