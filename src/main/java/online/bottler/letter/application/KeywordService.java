package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.letter.application.dto.response.KeywordResponseDTO;
import online.bottler.letter.application.port.out.KeywordRepository;
import online.bottler.letter.domain.Keyword;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public KeywordResponseDTO getKeywords() {
        List<Keyword> keywords = keywordRepository.getKeywords();
        return KeywordResponseDTO.from(keywords);
    }
}
