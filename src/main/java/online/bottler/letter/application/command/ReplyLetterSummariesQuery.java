package online.bottler.letter.application.command;

public record ReplyLetterSummariesQuery(Long letterId, CommonPageCommand commonPageCommand, Long userId) {
    public static ReplyLetterSummariesQuery of(Long letterId, CommonPageCommand commonPageCommand, Long userId) {
        return new ReplyLetterSummariesQuery(letterId, commonPageCommand, userId);
    }
}
