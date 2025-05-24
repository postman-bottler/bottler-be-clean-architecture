package online.bottler.mapletter.application.response;

public record CheckReplyMapLetterResponse(
        boolean isReplied //true면 답장을 이미 보낸 상태, false면 답장을 보내지 않은 상태
) {
}
