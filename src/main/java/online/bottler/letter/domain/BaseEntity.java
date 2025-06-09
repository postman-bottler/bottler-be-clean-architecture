package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class BaseEntity {
    private final Long id;
    private final LocalDateTime createdAt;

    protected BaseEntity(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
