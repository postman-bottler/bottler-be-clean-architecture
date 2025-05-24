package online.bottler.user.application.command;

public record ChangePasswordCommand(
        String existingPassword,
        String newPassword
) {
}
