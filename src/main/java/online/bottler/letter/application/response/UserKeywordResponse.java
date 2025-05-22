package online.bottler.letter.application.response;

import java.util.List;
import online.bottler.letter.domain.UserKeyword;

public record UserKeywordResponse(List<String> keywords) {
    public static UserKeywordResponse from(List<UserKeyword> userKeywords) {
        List<String> keywords = userKeywords.stream().map(UserKeyword::getKeyword).toList();

        return new UserKeywordResponse(keywords);
    }
}
