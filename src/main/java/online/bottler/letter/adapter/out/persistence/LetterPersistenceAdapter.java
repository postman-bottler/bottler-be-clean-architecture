package online.bottler.letter.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.out.CreateLetterPort;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements CreateLetterPort {

    @Override
    public Letter save(Letter letter) {
        return null;
    }
}
