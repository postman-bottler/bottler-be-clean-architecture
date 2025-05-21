package online.bottler.user.application.repository;

import online.bottler.user.domain.ProfileImage;

public interface ProfileImageRepository {
    void save(ProfileImage profileImage);

    boolean existsByUrl(String newProfileImage);

    String findProfileImage();
}
