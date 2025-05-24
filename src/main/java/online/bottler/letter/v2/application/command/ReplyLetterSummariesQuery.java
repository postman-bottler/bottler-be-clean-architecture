package online.bottler.letter.v2.application.command;


import org.springframework.data.domain.PageRequest;

public record ReplyLetterSummariesQuery(Long letterId, PageRequest pageRequest, Long userId) {
    public static ReplyLetterSummariesQuery of(Long letterId, PageRequest pageRequest, Long userId) {
        return null;
    }
}
