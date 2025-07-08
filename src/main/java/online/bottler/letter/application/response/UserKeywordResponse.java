package online.bottler.letter.application.response;

import java.util.List;

public record UserKeywordResponse(List<String> keywords) {
    public static UserKeywordResponse from(List<String> keywords) {
        return new UserKeywordResponse(keywords);
    }
}
