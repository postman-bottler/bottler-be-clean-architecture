package online.bottler.user.application.port.in;

import jakarta.mail.MessagingException;

public interface EmailUseCase {
    void sendEmail(String toEmail, String title, String content) throws MessagingException;
}
