package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;

@Entity
@Table(name = "letters",
        indexes = @Index(name = "idx_letter_status_id", columnList = ("status, id")))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterEntity extends BaseLetterEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    private LetterEntity(
            Long id,
            Long userId,
            LetterContentEntity letterContentEntity,
            LetterStatus status
    ) {
        this.id = id;
        this.userId = userId;
        this.letterContentEntity = letterContentEntity;
        this.status = status;
    }

    public static LetterEntity from(Letter letter) {
        return LetterEntity.builder()
                .id(letter.getId())
                .userId(letter.getUserId())
                .letterContentEntity(LetterContentEntity.from(letter.getLetterContent()))
                .status(letter.getStatus())
                .build();
    }

    public static List<LetterEntity> fromList(List<Letter> letters) {
        return letters.stream().map(LetterEntity::from).toList();
    }

    public Letter toDomain() {
        return Letter.of(
                id,
                userId,
                letterContentEntity.toDomain(),
                status,
                createdAt
        );
    }

    public static List<Letter> toDomainList(List<LetterEntity> letterEntities) {
        return letterEntities.stream().map(LetterEntity::toDomain).toList();
    }
}
