package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.RecommendedLetter;

@Entity
@Table(name = "recommended_letter",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_letter", columnNames = {"userId", "letterId"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedLetterEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Builder
    private RecommendedLetterEntity(Long id, Long userId, Long letterId) {
        this.id = id;
        this.userId = userId;
        this.letterId = letterId;
    }

    public static RecommendedLetterEntity from(RecommendedLetter recommendedLetter) {
        return RecommendedLetterEntity.builder()
                .id(recommendedLetter.getId())
                .userId(recommendedLetter.getUserId())
                .letterId(recommendedLetter.getLetterId())
                .build();
    }

    public RecommendedLetter toDomain() {
        return RecommendedLetter.of(id, userId, letterId);
    }
}
