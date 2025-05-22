package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.letter.adapter.in.web.request.UserKeywordRequest;
import online.bottler.letter.application.response.UserKeywordResponse;
import online.bottler.letter.application.port.out.UserKeywordRepository;
import online.bottler.letter.domain.UserKeyword;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;

    @Transactional(readOnly = true)
    public UserKeywordResponse findUserKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordRepository.findUserKeywordsByUserId(userId);
        return UserKeywordResponse.from(userKeywords);
    }

    @Transactional
    public void createKeywords(UserKeywordRequest userKeywordRequestDTO, Long userId) {
        List<UserKeyword> userKeywords = userKeywordRequestDTO.toDomain(userId);
        userKeywordRepository.replaceKeywordsByUserId(userKeywords, userId);
    }

    @Transactional(readOnly = true)
    public List<String> findKeywords(Long userId) {
        return userKeywordRepository.findKeywordsByUserId(userId);
    }
}
