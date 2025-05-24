package online.bottler.letter.domain;


import lombok.Getter;

@Getter
public class UserKeyword {

    private Long id;
    private final Long userId;
    private final String keyword;

    public UserKeyword(Long id, Long userId, String keyword) {
        this.id = id;
        this.userId = userId;
        this.keyword = keyword;
    }

    public static UserKeyword of(Long id, Long userId, String keyword) {
        return new UserKeyword(id, userId, keyword);
    }

    public static UserKeyword create(Long userId, String keyword) {
        return new UserKeyword(null, userId, keyword);
    }
}
