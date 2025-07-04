package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterStatus;

@Entity
@Table(name = "letter_keyword",
        indexes = @Index(name = "idx_letterkeyword_keyword_status_letter", columnList = "keyword, status, letterId"),
        uniqueConstraints = @UniqueConstraint(name = "uq_letter_keyword", columnNames = {"letterId", "keyword"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterKeywordEntity extends BaseEntity {

    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LetterStatus status;

    @Builder
    private LetterKeywordEntity(Long id, Long letterId, String keyword, LetterStatus status) {
        this.id = id;
        this.letterId = letterId;
        this.keyword = keyword;
        this.status = status;
    }

    public static LetterKeywordEntity from(LetterKeyword letterKeyword) {
        return LetterKeywordEntity.builder()
                .id(letterKeyword.getId())
                .letterId(letterKeyword.getLetterId())
                .keyword(letterKeyword.getKeyword())
                .status(letterKeyword.getStatus())
                .build();
    }

    public static List<LetterKeywordEntity> fromList(List<LetterKeyword> letterKeywords) {
        return letterKeywords.stream()
                .map(LetterKeywordEntity::from)
                .toList();
    }

    public LetterKeyword toDomain() {
        return LetterKeyword.of(id, letterId, keyword, status, createdAt);
    }

    public static List<LetterKeyword> toDomainList(List<LetterKeywordEntity> letterKeywordEntities) {
        return letterKeywordEntities.stream()
                .map(LetterKeywordEntity::toDomain)
                .toList();
    }
}
