package online.bottler.mapletter.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;

public interface MapLetterGuestUseCase {
    List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude);

    OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude);
}
