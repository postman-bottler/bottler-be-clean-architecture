package online.bottler.letter.application;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.GetAllKeywordsUseCase;
import online.bottler.letter.application.response.KeywordResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordServiceV2 implements GetAllKeywordsUseCase {
    @Override
    public KeywordResponse getAll() {
        return null;
    }
}
