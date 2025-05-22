package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterKeyword;

@Entity
@Table(
        name = "letter_keyword",
        indexes = @Index(name = "idx_letterkeyword_keyword_isdeleted_letter",
                columnList = "keyword, isDeleted, letterId"),
        uniqueConstraints = @UniqueConstraint(name = "uq_letter_keyword", columnNames = {"letterId", "keyword"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterKeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long letterId;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public LetterKeywordEntity(Long letterId, String keyword, boolean isDeleted) {
        this.letterId = letterId;
        this.keyword = keyword;
        this.isDeleted = isDeleted;
    }

    public static LetterKeywordEntity from(LetterKeyword letterKeyword) {
        return LetterKeywordEntity.builder()
                .letterId(letterKeyword.getLetterId())
                .keyword(letterKeyword.getKeyword())
                .isDeleted(letterKeyword.isDeleted())
                .build();
    }

    public LetterKeyword toDomain() {
        return LetterKeyword.of(id, letterId, keyword, isDeleted);
    }
}
