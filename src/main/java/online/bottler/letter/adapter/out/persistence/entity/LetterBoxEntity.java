package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterBox;

@Entity
@Table(name = "letter_box",
        indexes = @Index(name = "idx_letterbox_user_box_createdat", columnList = "userId, boxType, createdAt DESC"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterBoxEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Embedded
    private LetterBoxTypeEntity letterBoxTypeEntity;

    @Builder
    private LetterBoxEntity(
            Long id,
            Long userId,
            Long letterId,
            LetterBoxTypeEntity letterBoxTypeEntity
    ) {
        this.id = id;
        this.userId = userId;
        this.letterId = letterId;
        this.letterBoxTypeEntity = letterBoxTypeEntity;
    }

    public static LetterBoxEntity from(LetterBox letterBox) {
        return LetterBoxEntity.builder()
                .id(letterBox.getId())
                .userId(letterBox.getUserId())
                .letterId(letterBox.getLetterId())
                .letterBoxTypeEntity(LetterBoxTypeEntity.from(letterBox.getLetterBoxType()))
                .build();
    }

    public LetterBox toDomain() {
        return LetterBox.of(
                id,
                userId,
                letterId,
                letterBoxTypeEntity.toDomain(),
                createdAt
        );
    }
}
