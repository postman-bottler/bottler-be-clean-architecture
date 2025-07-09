package online.bottler.letter.domain;

public record Keyword(Long id, String keyword, String category) {

    public static Keyword of(Long id, String keyword, String category) {
        return new Keyword(id, keyword, category);
    }

    public static Keyword from(String keyword, String category) {
        return new Keyword(null, keyword, category);
    }
}
