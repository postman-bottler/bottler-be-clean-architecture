package online.bottler.letter.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendedLetter {

    private final Long id;
    private final Long userId;
    private final Long letterId;
}
