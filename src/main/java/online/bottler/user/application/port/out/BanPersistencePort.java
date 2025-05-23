package online.bottler.user.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import online.bottler.user.domain.Ban;

public interface BanPersistencePort {
    Ban save(Ban ban);

    Optional<Ban> findByUserId(Long userId);

    List<Ban> findExpiredBans(LocalDateTime now);

    void deleteBans(List<Ban> bans);

    Ban updateBan(Ban ban);
}
