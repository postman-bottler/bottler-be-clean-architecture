package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterBox;

@Entity
@Table(name = "letter_box",
        indexes = @Index(name = "idx_letterbox_user_box_createdat", columnList = "userId, boxType, createdAt DESC"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long letterId;

    @Embedded
    private LetterBoxTypeEntity letterBoxTypeEntity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private LetterBoxEntity(Long userId, Long letterId, LetterBoxTypeEntity letterBoxTypeEntity, LocalDateTime createdAt) {
        this.userId = userId;
        this.letterId = letterId;
        this.letterBoxTypeEntity = letterBoxTypeEntity;
        this.createdAt = createdAt;
    }

    public static LetterBoxEntity from(LetterBox letterBox) {
        return LetterBoxEntity.builder()
                .userId(letterBox.getUserId())
                .letterId(letterBox.getLetterId())
                .letterBoxTypeEntity(LetterBoxTypeEntity.from(letterBox.getLetterBoxType()))
                .createdAt(letterBox.getCreatedAt())
                .build();
    }

    public LetterBox toDomain() {
        return LetterBox.of(id, userId, letterId, letterBoxTypeEntity.toDomain(), createdAt);
    }
}
