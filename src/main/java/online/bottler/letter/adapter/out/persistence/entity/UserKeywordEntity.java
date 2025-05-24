package online.bottler.letter.adapter.out.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.UserKeyword;

@Entity
@Table(
        name = "user_keyword",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_keyword", columnNames = {"userId", "keyword"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserKeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String keyword;

    @Builder
    public UserKeywordEntity(Long userId, String keyword) {
        this.userId = userId;
        this.keyword = keyword;
    }

    public static UserKeywordEntity from(UserKeyword userKeyword) {
        return UserKeywordEntity.builder()
                .userId(userKeyword.getUserId())
                .keyword(userKeyword.getKeyword())
                .build();
    }

    public UserKeyword toDomain() {
        return UserKeyword.of(id, userId, keyword);
    }
}
