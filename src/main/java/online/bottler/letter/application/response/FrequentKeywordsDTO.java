package online.bottler.letter.application.response;

import java.util.List;

public record FrequentKeywordsDTO(List<String> keywords) {
    public static FrequentKeywordsDTO from(List<String> keywords) {
        return new FrequentKeywordsDTO(keywords);
    }
}
