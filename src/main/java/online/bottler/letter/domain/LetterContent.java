package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public class LetterContent {
    private final String title;
    private final String content;
    private final String font;
    private final String paper;
    private final String label;

    private LetterContent(String title, String content, String font, String paper, String label) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
    }

    public static LetterContent of(String title, String content, String font, String paper, String label) {
        return new LetterContent(validateTitle(title), content, font, paper, label);
    }

    private static String validateTitle(String title) {
        return (title == null || title.trim().isEmpty()) ? "무제" : title;
    }
}
