package online.bottler.letter.domain;

public record UserKeyword(Long id, Long userId, String keyword) {

    public static UserKeyword of(Long id, Long userId, String keyword) {
        return new UserKeyword(id, userId, keyword);
    }

    public static UserKeyword create(Long userId, String keyword) {
        return new UserKeyword(null, userId, keyword);
    }
}
