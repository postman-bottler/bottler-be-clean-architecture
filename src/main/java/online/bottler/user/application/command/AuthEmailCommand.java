package online.bottler.user.application.command;

public record AuthEmailCommand(
        String email,
        String code
) {
}
