package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import online.bottler.letter.domain.LetterStatus;

@MappedSuperclass
abstract class BaseLetterEntity extends BaseEntity {

    @Embedded
    protected LetterContentEntity letterContentEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected LetterStatus status;
}
