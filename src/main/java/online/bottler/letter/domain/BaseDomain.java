package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
abstract class BaseDomain {
    private final Long id;
    private final LocalDateTime createdAt;

    BaseDomain(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
