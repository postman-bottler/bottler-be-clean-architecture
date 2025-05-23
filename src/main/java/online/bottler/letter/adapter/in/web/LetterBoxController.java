package online.bottler.letter.adapter.in.web;

import static online.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.adapter.in.web.annotation.LetterValidationMetaData;
import online.bottler.letter.application.port.in.GetAllLettersUseCase;
import online.bottler.letter.application.port.in.GetReceivedLettersUseCase;
import online.bottler.letter.application.port.in.GetSentLettersUseCase;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import online.bottler.global.response.ApiResponse;
import online.bottler.letter.application.command.LetterDeleteDTO;
import online.bottler.letter.adapter.in.web.request.PageRequest;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.response.PageResponse;
import online.bottler.letter.application.LetterDeletionService;
import online.bottler.user.auth.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/letters/saved")
@RequiredArgsConstructor
@Tag(name = "Letter Box", description = "보관된(saved) 편지 관리 API")
public class LetterBoxController {

    private final LetterDeletionService letterDeletionService;

    private final GetAllLettersUseCase getAllLettersUseCase;
    private final GetSentLettersUseCase getSentLettersUseCase;
    private final GetReceivedLettersUseCase getReceivedLettersUseCase;

    @Operation(summary = "보관된 모든 편지 조회", description = "페이지네이션을 사용하여 보관된 모든 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
            + "\nPage Default: page(1) size(9) sort(createAt)")
    @GetMapping
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponse<LetterSummaryResponse>> getAllLetters(@Valid PageRequest pageRequestDTO,
                                                                          BindingResult bindingResult,
                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(
                PageResponse.from(getAllLettersUseCase.getAllLetters(pageRequestDTO, userDetails.getUserId())));
    }

    @Operation(summary = "보낸 편지 조회", description = "페이지네이션을 사용하여 보관된 보낸 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
            + "\nPage Default: page(1) size(9) sort(createAt)")
    @GetMapping("/sent")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponse<LetterSummaryResponse>> getSentLetters(@Valid PageRequest pageRequestDTO,
                                                                           BindingResult bindingResult,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(PageResponse.from(
                getSentLettersUseCase.getSentLetters(pageRequestDTO, userDetails.getUserId())));
    }

    @Operation(summary = "받은 편지 조회", description = "페이지네이션을 사용하여 보관된 받은 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
            + "\nPage Default: page(1) size(9) sort(createAt)")
    @GetMapping("/received")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponse<LetterSummaryResponse>> getReceivedLetters(
            @Valid PageRequest pageRequest, BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(PageResponse.from(
                getReceivedLettersUseCase.getReceivedLetters(pageRequest, userDetails.getUserId())));
    }

    @Operation(summary = "보관된 편지 삭제", description = "편지ID, 편지타입(LETTER, REPLY_LETTER), 송수신 타입(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteSavedLetter(@RequestBody @Valid List<LetterDeleteDTO> letterDeleteDTOS,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        letterDeletionService.deleteLetters(letterDeleteDTOS, userDetails.getUserId());
        return ApiResponse.onSuccess("보관된 편지를 삭제했습니다.");
    }

    @DeleteMapping("/all")
    public ApiResponse<String> deleteAllSavedLetters(@AuthenticationPrincipal CustomUserDetails userDetails) {
        letterDeletionService.deleteAllSavedLetters(userDetails.getUserId());
        return ApiResponse.onSuccess("보관된 편지를 모두 삭제했습니다");
    }

    @DeleteMapping("/received")
    public ApiResponse<String> deleteAllSavedReceivedLetters(@AuthenticationPrincipal CustomUserDetails userDetails) {
        letterDeletionService.deleteAllSavedReceivedLetters(userDetails.getUserId());
        return ApiResponse.onSuccess("받은 편지를 모두 삭제했습니다");
    }

    @DeleteMapping("/sent")
    public ApiResponse<String> deleteAllSavedSentLetters(@AuthenticationPrincipal CustomUserDetails userDetails) {
        letterDeletionService.deleteAllSavedSentLetters(userDetails.getUserId());
        return ApiResponse.onSuccess("보낸 편지를 모두 삭제했습니다.");
    }
}
