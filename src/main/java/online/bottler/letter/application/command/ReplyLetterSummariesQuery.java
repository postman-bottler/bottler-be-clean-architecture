package online.bottler.letter.application.command;

public record ReplyLetterSummariesQuery(Long userId, Long letterId, CommonPageCommand commonPageCommand) {
    public static ReplyLetterSummariesQuery of(Long userId, Long letterId, CommonPageCommand commonPageCommand) {
        return new ReplyLetterSummariesQuery(userId, letterId, commonPageCommand);
    }
}
