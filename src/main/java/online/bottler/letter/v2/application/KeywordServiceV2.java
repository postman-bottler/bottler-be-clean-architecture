package online.bottler.letter.v2.application;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.v2.application.port.in.GetAllKeywordsUseCase;
import online.bottler.letter.v2.application.response.KeywordResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordServiceV2 implements GetAllKeywordsUseCase {
    @Override
    public KeywordResponse getAll() {
        return null;
    }
}
