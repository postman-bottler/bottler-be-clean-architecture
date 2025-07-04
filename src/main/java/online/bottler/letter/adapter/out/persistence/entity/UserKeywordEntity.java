package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
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
public class UserKeywordEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Builder
    public UserKeywordEntity(Long id, Long userId, String keyword) {
        this.id = id;
        this.userId = userId;
        this.keyword = keyword;
    }

    public static UserKeywordEntity from(UserKeyword userKeyword) {
        return UserKeywordEntity.builder()
                .id(userKeyword.getId())
                .userId(userKeyword.getUserId())
                .keyword(userKeyword.getKeyword())
                .build();
    }

    public static List<UserKeywordEntity> fromList(List<UserKeyword> userKeywords) {
        return userKeywords.stream().map(UserKeywordEntity::from).toList();
    }

    public UserKeyword toDomain() {
        return UserKeyword.of(id, userId, keyword);
    }

    public static List<UserKeyword> toDomainList(List<UserKeywordEntity> userKeywordEntities) {
        return userKeywordEntities.stream().map(UserKeywordEntity::toDomain).toList();
    }
}
