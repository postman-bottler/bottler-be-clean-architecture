package online.bottler.letter.domain;

public record LetterContent(
        String title,
        String content,
        String font,
        String paper,
        String label
) {

    public static LetterContent of(
            String title,
            String content,
            String font,
            String paper,
            String label
    ) {
        return new LetterContent(
                validateTitle(title),
                content,
                font,
                paper,
                label
        );
    }

    private static String validateTitle(String title) {
        return (title == null || title.trim().isEmpty()) ? "무제" : title;
    }
}
