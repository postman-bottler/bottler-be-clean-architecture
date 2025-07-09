package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterType;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterBoxTypeEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "letter_type", nullable = false)
    private LetterType letterType;

    @Enumerated(EnumType.STRING)
    @Column(name = "box_type", nullable = false)
    private BoxType boxType;

    @Builder
    private LetterBoxTypeEntity(LetterType letterType, BoxType boxType) {
        this.letterType = letterType;
        this.boxType = boxType;
    }

    public static LetterBoxTypeEntity from(LetterBoxType letterBoxType) {
        return LetterBoxTypeEntity.builder()
                .letterType(letterBoxType.letterType())
                .boxType(letterBoxType.boxType())
                .build();
    }

    public LetterBoxType toDomain() {
        return LetterBoxType.of(
                letterType,
                boxType
        );
    }
}
