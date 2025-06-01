package online.bottler.letter.application.response;

import java.util.List;

public record FrequentKeywordsResponse(List<String> keywords) {
    public static FrequentKeywordsResponse from(List<String> keywords) {
        return new FrequentKeywordsResponse(keywords);
    }
}
