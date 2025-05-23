package online.bottler.letter.adapter.in.web;

import static online.bottler.global.response.code.ErrorStatus.LETTER_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.in.web.annotation.LetterValidationMetaData;
import online.bottler.letter.adapter.in.web.request.LetterWithKeywordsDeleteRequest;
import online.bottler.letter.adapter.in.web.request.LetterWithKeywordsRequest;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.port.in.CreateLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.DeleteLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.GetLetterWithKeywordsDetailUseCase;
import online.bottler.letter.application.port.in.GetRecommendedLettersUseCase;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import online.bottler.global.response.ApiResponse;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import online.bottler.user.auth.CustomUserDetails;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
@Tag(name = "키워드 편지", description = "키워드 편지 API")
public class LetterWithKeywordsController {

    private final CreateLetterWithKeywordsUseCase createLetterWithKeywordsUseCase;
    private final GetLetterWithKeywordsDetailUseCase getLetterWithKeywordsDetailUseCase;
    private final GetRecommendedLettersUseCase getRecommendedLettersUseCase;
    private final DeleteLetterWithKeywordsUseCase deleteLetterWithKeywordsUseCase;

    @Operation(summary = "키워드 편지 생성", description = "새로운 키워드 편지를 생성합니다.")
    @PostMapping
    @LetterValidationMetaData(message = "키워드 편지 유효성 검사 실패", errorStatus = LETTER_VALIDATION_ERROR)
    public ApiResponse<LetterWithKeywordsResponse> createLetter(
            @RequestBody @Valid LetterWithKeywordsRequest letterWithKeywordsRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onCreateSuccess(
                createLetterWithKeywordsUseCase.create(letterWithKeywordsRequest.toCommand(userDetails.getUserId())));
    }

    @Operation(summary = "키워드 편지 상세 조회", description = "편지 ID로 키워드 편지의 상세 정보를 조회합니다.")
    @GetMapping("/detail/{letterId}")
    public ApiResponse<LetterWithKeywordsDetailResponse> getLetterDetail(@PathVariable Long letterId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(getLetterWithKeywordsDetailUseCase.getDetail(
                LetterWithKeywordsDetailQuery.of(letterId, userDetails.getUserId())));
    }

    @Operation(summary = "추천 키워드 편지 조회", description = "사용자에게 현재 추천된 키워드 편지들의 정보를 제공합니다.")
    @GetMapping("/recommend")
    public ApiResponse<List<LetterRecommendSummaryResponse>> getRecommendLetters(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(getRecommendedLettersUseCase.getRecommended(userDetails.getUserId()));
    }

    @Operation(summary = "키워드 편지 삭제", description = "키워드 편지ID, BoxType 송수신(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteLetter(@RequestBody @Valid LetterWithKeywordsDeleteRequest letterWithKeywordsDeleteRequest,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        deleteLetterWithKeywordsUseCase.delete(letterWithKeywordsDeleteRequest.toCommand(userDetails.getUserId()));
        return ApiResponse.onSuccess("키워드 편지를 삭제했습니다.");
    }
}
