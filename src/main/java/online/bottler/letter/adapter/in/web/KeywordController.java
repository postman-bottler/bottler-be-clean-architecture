package online.bottler.letter.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.bottler.auth.CustomUserDetails;
import online.bottler.global.response.ApiResponse;
import online.bottler.letter.adapter.in.web.request.UserKeywordRequest;
import online.bottler.letter.application.port.in.KeywordUseCase;
import online.bottler.letter.application.port.in.LetterKeywordUseCase;
import online.bottler.letter.application.port.in.UserKeywordUseCase;
import online.bottler.letter.application.response.FrequentKeywordsResponse;
import online.bottler.letter.application.response.KeywordResponse;
import online.bottler.letter.application.response.UserKeywordResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {

    private final UserKeywordUseCase userKeywordUseCase;
    private final KeywordUseCase keywordUseCase;
    private final LetterKeywordUseCase letterKeywordUseCase;

    @Operation(summary = "유저 키워드 등록", description = "유저가 설정한 키워드로 변경합니다.")
    @PostMapping
    public ApiResponse<String> createKeywords(@RequestBody UserKeywordRequest userKeywordRequest,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        userKeywordUseCase.create(userKeywordRequest.toCommand(userDetails.getUserId()));
        return ApiResponse.onSuccess("사용자 키워드를 생성하였습니다.");
    }

    @Operation(summary = "전체 키워드 조회", description = "카테고리별로 등록된 키워드 목록을 조회합니다.")
    @GetMapping("/list")
    public ApiResponse<KeywordResponse> getKeywordList() {
        return ApiResponse.onSuccess(keywordUseCase.getAll());
    }

    @Operation(summary = "유저 키워드 목록 조회", description = "유저가 설정한 키워드 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<UserKeywordResponse> getKeywords(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(userKeywordUseCase.getKeywords(userDetails.getUserId()));
    }

    @Operation(summary = "사용자의 자주 쓰는 키워드 조회", description = "현재 사용자의 자주 쓰는 키워드를 조회합니다")
    @GetMapping("/frequent")
    public ApiResponse<FrequentKeywordsResponse> getTopFrequentKeywords(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(letterKeywordUseCase.getTopFrequent(userDetails.getUserId()));
    }
}
