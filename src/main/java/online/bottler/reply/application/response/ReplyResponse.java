package online.bottler.reply.application.response;

import online.bottler.reply.application.ReplyType;

public record ReplyResponse(
        ReplyType type,
        String labelUrl,
        Long letterId
) {
    public static ReplyResponse from(ReplyType replyType, String labelUrl, Long letterId) {
        return new ReplyResponse(replyType, labelUrl, letterId);
    }
}
