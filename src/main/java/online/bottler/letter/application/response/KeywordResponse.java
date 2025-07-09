package online.bottler.letter.application.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.Keyword;

public record KeywordResponse(List<CategoryKeywordsResponse> categories) {
    public static KeywordResponse from(List<Keyword> keywordList) {
        Map<String, List<String>> groupedByCategory = keywordList.stream().collect(
                Collectors.groupingBy(Keyword::category,
                        Collectors.mapping(Keyword::keyword, Collectors.toList())));

        List<CategoryKeywordsResponse> categories = groupedByCategory.entrySet().stream()
                .map(entry -> new CategoryKeywordsResponse(entry.getKey(), entry.getValue())).toList();

        return new KeywordResponse(categories);
    }

    public record CategoryKeywordsResponse(String category, List<String> keywords) {
    }
}
