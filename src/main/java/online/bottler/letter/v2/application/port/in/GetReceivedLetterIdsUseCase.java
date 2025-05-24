package online.bottler.letter.v2.application.port.in;

import java.util.List;

public interface GetReceivedLetterIdsUseCase {
    List<Long> getReceivedLetterIdsByUserId(String userId);
}
