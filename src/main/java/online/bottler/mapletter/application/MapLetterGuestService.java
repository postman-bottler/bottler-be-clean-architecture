package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterGuestUseCase;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.user.application.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MapLetterGuestService implements MapLetterGuestUseCase {

    private final MapLetterPersistencePort mapLetterPersistencePort;
    private final UserService userService;

    private static final double VIEW_DISTANCE = 15;

    @Override
    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        List<MapLetterAndDistance> letters = mapLetterPersistencePort.guestFindLettersByUserLocation(latitude,
                longitude);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponse.from(letter, nickname);
                        }
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);

        Double distance = mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);

        mapLetter.isPrivate();

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, false, false, false);
    }
}
