package online.bottler.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.user.domain.ProfileImage;
import online.bottler.user.exception.ProfileImageException;
import online.bottler.user.infra.entity.ProfileImageEntity;
import online.bottler.user.application.repository.ProfileImageRepository;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {
    private final ProfileImageJpaRepository profileImageJpaRepository;

    @Override
    public void save(ProfileImage profileImage) {
        profileImageJpaRepository.save(ProfileImageEntity.from(profileImage));
    }

    @Override
    public boolean existsByUrl(String newProfileImage) {
        return profileImageJpaRepository.existsByImageUrl(newProfileImage);
    }

    @Override
    public String findProfileImage() {
        String profileImageUrl = profileImageJpaRepository.findRandomProfileImage();
        if (profileImageUrl == null) {
            throw new ProfileImageException("프로필 이미지를 찾을 수 없습니다.");
        }
        return profileImageUrl;
    }
}
