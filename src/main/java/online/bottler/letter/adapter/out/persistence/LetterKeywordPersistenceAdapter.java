package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.out.CreateLetterKeywordPort;
import online.bottler.letter.domain.LetterKeyword;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordPersistenceAdapter implements CreateLetterKeywordPort {

    @Override
    public void saveAll(List<LetterKeyword> letterKeywords) {

    }
}
