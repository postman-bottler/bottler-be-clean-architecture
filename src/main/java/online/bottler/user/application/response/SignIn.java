package online.bottler.user.application.response;

public record SignIn(
        String accessToken,
        String refreshToken
) {
}
