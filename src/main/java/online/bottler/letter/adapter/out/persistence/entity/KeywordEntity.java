package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.Keyword;

@Entity
@Table(name = "keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
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
}
