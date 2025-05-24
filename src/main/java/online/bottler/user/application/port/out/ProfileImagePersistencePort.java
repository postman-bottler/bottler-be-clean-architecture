package online.bottler.user.application.port.out;


import online.bottler.user.domain.ProfileImage;

public interface ProfileImagePersistencePort {
    void save(ProfileImage profileImage);

    boolean existsByUrl(String newProfileImage);

    String findProfileImage();
}
