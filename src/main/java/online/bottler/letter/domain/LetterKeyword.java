package online.bottler.letter.domain;


import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class LetterKeyword {

    private Long id;
    private final Long letterId;
    private final String keyword;
    private final boolean isDeleted;

    public LetterKeyword(Long id, Long letterId, String keyword, boolean isDeleted) {
        this.id = id;
        this.letterId = letterId;
        this.keyword = keyword;
        this.isDeleted = isDeleted;
    }

    public static LetterKeyword of(Long id, Long letterId, String keyword, boolean isDeleted) {
        return new LetterKeyword(id, letterId, keyword, isDeleted);
    }

    public static LetterKeyword create(Long letterId, String keyword) {
        return new LetterKeyword(null, letterId, keyword, false);
    }

    public static List<LetterKeyword> createList(Long letterId, List<String> keywords) {
        return keywords.stream().map(keyword -> create(letterId, keyword)).collect(Collectors.toList());
    }
}

