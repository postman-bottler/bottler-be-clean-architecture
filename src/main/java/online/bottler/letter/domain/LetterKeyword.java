package online.bottler.letter.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LetterKeyword {

    private final Long id;
    private final Long letterId;
    private final String keyword;
    private final boolean isDeleted;

    public static LetterKeyword from(Long letterId, String keyword) {
        return LetterKeyword.builder().letterId(letterId).keyword(keyword).isDeleted(false).build();
    }
}
