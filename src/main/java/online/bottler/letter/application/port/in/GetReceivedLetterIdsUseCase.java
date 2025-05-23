package online.bottler.letter.application.port.in;

import java.util.List;

public interface GetReceivedLetterIdsUseCase {
    List<Long> getReceivedLetterIdsByUserId(String userId);
}
