package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;

@Entity
@Table(
        name = "letter_box",
        indexes = @Index(name = "idx_letterbox_user_box_createdat",
                columnList = "userId, boxType, createdAt DESC")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long letterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LetterType letterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoxType boxType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private LetterBoxEntity(Long userId, Long letterId, LetterType letterType, BoxType boxType,
                            LocalDateTime createdAt) {
        this.userId = userId;
        this.letterId = letterId;
        this.letterType = letterType;
        this.boxType = boxType;
        this.createdAt = createdAt;
    }

    public static LetterBoxEntity from(LetterBox letterBox) {
        return LetterBoxEntity.builder()
                .userId(letterBox.getUserId())
                .letterId(letterBox.getLetterId())
                .letterType(letterBox.getLetterType())
                .boxType(letterBox.getBoxType())
                .createdAt(letterBox.getCreatedAt())
                .build();
    }

    public LetterBox toDomain() {
        return LetterBox.of(id, userId, letterId, letterType, boxType, createdAt);
    }
}
