package online.bottler.user.application.command;

public record SignUpCommand(
        String email,
        String password,
        String nickname
) {
}