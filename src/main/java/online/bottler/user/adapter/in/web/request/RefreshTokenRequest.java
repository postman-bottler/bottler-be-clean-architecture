package online.bottler.user.adapter.in.web.request;

import online.bottler.user.application.command.RefreshTokenCommand;

public record RefreshTokenRequest(
        String refreshToken
) {
    public RefreshTokenCommand toCommand() {
        return new RefreshTokenCommand(refreshToken);
    }
}
