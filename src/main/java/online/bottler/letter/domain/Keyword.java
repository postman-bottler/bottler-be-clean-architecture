package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public class Keyword {

    private Long id;
    private final String keyword;
    private final String category;

    public Keyword(Long id, String keyword, String category) {
        this.id = id;
        this.keyword = keyword;
        this.category = category;
    }

    public static Keyword of(Long id, String keyword, String category) {
        return new Keyword(id, keyword, category);
    }

    public static Keyword from(String keyword, String category) {
        return new Keyword(null, keyword, category);
    }
}
