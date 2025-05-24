package online.bottler.user.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import online.bottler.user.application.port.out.BanPersistencePort;
import online.bottler.user.adapter.out.persistence.entity.BanEntity;
import online.bottler.user.adapter.out.persistence.repository.BanJpaRepository;
import online.bottler.user.domain.Ban;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BanPersistenceAdapter implements BanPersistencePort {
    private final BanJpaRepository banJpaRepository;

    @Override
    public Ban save(Ban ban) {
        BanEntity entity = BanEntity.from(ban);
        return banJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Ban> findByUserId(Long userId) {
        Optional<BanEntity> find = banJpaRepository.findById(userId);
        return find.map(BanEntity::toDomain);
    }

    @Override
    public List<Ban> findExpiredBans(LocalDateTime now) {
        return banJpaRepository.findByUnbansAtBefore(now).stream()
                .map(BanEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteBans(List<Ban> bans) {
        banJpaRepository.deleteAll(bans.stream().map(BanEntity::from).toList());
    }

    @Override
    public Ban updateBan(Ban ban) {
        return banJpaRepository.save(BanEntity.from(ban)).toDomain();
    }
}
