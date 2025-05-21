package online.bottler.scheduler;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import online.bottler.user.application.BanService;

@Component
@RequiredArgsConstructor
public class UnbanScheduler {
    private final BanService banService;

    public void unbanUsers(LocalDateTime unbansAt) {
        banService.unbans(unbansAt);
    }
}
