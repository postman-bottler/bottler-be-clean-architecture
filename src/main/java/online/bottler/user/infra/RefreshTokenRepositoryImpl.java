package online.bottler.user.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.user.domain.RefreshToken;
import online.bottler.user.exception.TokenException;
import online.bottler.user.infra.entity.RefreshTokenEntity;
import online.bottler.user.application.repository.RefreshTokenRepository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void createRefreshToken(RefreshToken refreshToken) {
        Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenJpaRepository.findByEmail(
                refreshToken.getEmail());

        if (refreshTokenEntityOpt.isEmpty()) {
            refreshTokenJpaRepository.save(RefreshTokenEntity.from(refreshToken));
        } else {
            RefreshTokenEntity refreshTokenEntity = refreshTokenEntityOpt.get();
            refreshTokenEntity.updateRefreshToken(refreshToken.getRefreshToken());
            refreshTokenJpaRepository.save(refreshTokenEntity);
        }
    }

    @Override
    public String findEmailByRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenJpaRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenException("유효하지 않은 jwt 토큰입니다."));
        return refreshTokenEntity.getEmail();
    }

    @Override
    public void deleteByEmail(String email) {
        refreshTokenJpaRepository.deleteByEmail(email);
    }
}
