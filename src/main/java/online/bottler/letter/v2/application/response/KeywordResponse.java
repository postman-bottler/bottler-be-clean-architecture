package online.bottler.letter.v2.application.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.Keyword;

public record KeywordResponse(List<CategoryKeywordsDTO> categories) {
    public static KeywordResponse from(List<Keyword> keywordList) {
        Map<String, List<String>> groupedByCategory = keywordList.stream().collect(
                Collectors.groupingBy(Keyword::getCategory,
                        Collectors.mapping(Keyword::getKeyword, Collectors.toList())));

        List<CategoryKeywordsDTO> categories = groupedByCategory.entrySet().stream()
                .map(entry -> new CategoryKeywordsDTO(entry.getKey(), entry.getValue())).toList();

        return new KeywordResponse(categories);
    }

    public record CategoryKeywordsDTO(String category, List<String> keywords) {
    }
}
