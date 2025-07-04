package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.Keyword;

@Entity
@Table(name = "keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordEntity extends BaseEntity {

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "category", nullable = false)
    private String category;

    @Builder
    private KeywordEntity(Long id, String keyword, String category) {
        this.id = id;
        this.keyword = keyword;
        this.category = category;
    }

    public Keyword toDomain() {
        return Keyword.of(id, keyword, category);
    }

    public static List<Keyword> toDomainList(List<KeywordEntity> keywordEntities) {
        return keywordEntities.stream().map(KeywordEntity::toDomain).toList();
    }
}
