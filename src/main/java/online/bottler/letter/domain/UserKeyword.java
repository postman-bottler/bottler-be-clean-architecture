package online.bottler.letter.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserKeyword {

    private final Long id;
    private final Long userId;
    private final String keyword;
}
