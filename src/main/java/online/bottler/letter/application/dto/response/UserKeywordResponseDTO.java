package online.bottler.letter.application.dto.response;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public record UserKeywordResponseDTO(List<String> keywords) {
    public static UserKeywordResponseDTO from(List<UserKeyword> userKeywords) {
        List<String> keywords = userKeywords.stream().map(UserKeyword::getKeyword).toList();

        return new UserKeywordResponseDTO(keywords);
    }
}
