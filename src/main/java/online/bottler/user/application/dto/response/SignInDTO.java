package online.bottler.user.application.dto.response;

public record SignInDTO(
        String accessToken,
        String refreshToken
) {
}
