package online.bottler.user.application.response;


import online.bottler.user.domain.Provider;
import online.bottler.user.domain.User;

public record UserResponse(
        String email,
        String nickname,
        String profileImageUrl,
        Provider provider,
        int warningCount
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getEmail(), user.getNickname(), user.getImageUrl(), user.getProvider(),
                user.getWarningCount());
    }
}
