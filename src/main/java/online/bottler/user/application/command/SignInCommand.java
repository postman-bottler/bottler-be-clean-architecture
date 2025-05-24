package online.bottler.user.application.command;

public record SignInCommand(
        String email,
        String password
) {
}
