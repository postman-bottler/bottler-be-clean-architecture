package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LetterBox {
    private final Long id;
    private final Long userId;
    private final Long letterId;
    private final LetterType letterType;
    private final BoxType boxType;
    private final LocalDateTime createdAt;
}
