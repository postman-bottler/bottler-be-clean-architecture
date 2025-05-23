package online.bottler.user.application.port.in;

import java.time.LocalDateTime;
import online.bottler.user.domain.User;

public interface BanUseCase {
    void banUser(User user);
    void unbans(LocalDateTime now);
}
