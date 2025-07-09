package online.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class LetterKeyword extends BaseDomain {

    private final Long letterId;

    private final String keyword;

    private LetterStatus status;

    private LetterKeyword(Long id, Long letterId, String keyword, LetterStatus status, LocalDateTime createdAt) {
        super(id, createdAt);
        this.letterId = letterId;
        this.keyword = keyword;
        this.status = status;
    }

    public static LetterKeyword of(Long id, Long letterId, String keyword, LetterStatus status,
                                   LocalDateTime createdAt) {
        return new LetterKeyword(id, letterId, keyword, status, createdAt);
    }

    public static LetterKeyword create(Long letterId, String keyword) {
        return new LetterKeyword(null, letterId, keyword, LetterStatus.OPEN, null);
    }

    public static List<LetterKeyword> createList(Long letterId, List<String> keywords) {
        return keywords.stream().map(keyword -> create(letterId, keyword)).collect(Collectors.toList());
    }

    public void block() {
        this.status = LetterStatus.BLOCKED;
    }

    public void delete() {
        this.status = LetterStatus.DELETED;
    }
}
