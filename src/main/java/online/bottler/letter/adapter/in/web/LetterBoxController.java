package online.bottler.letter.adapter.in.web;

import static online.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.auth.CustomUserDetails;
import online.bottler.global.response.ApiResponse;
import online.bottler.letter.adapter.in.web.annotation.LetterValidationMetaData;
import online.bottler.letter.adapter.in.web.request.CommonPageRequest;
import online.bottler.letter.adapter.in.web.request.LetterDeleteRequest;
import online.bottler.letter.application.facade.LetterBoxFacade;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.response.PageResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/letterbox")
@RequiredArgsConstructor
@Tag(name = "Letter Box", description = "보관된(saved) 편지 관리 API")
public class LetterBoxController {

    private final LetterBoxFacade letterBoxFacade;


    @Operation(
            summary = "보관된 편지 조회",
            description =
                """
                페이지네이션을 사용하여 보관된 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다.\
                Page Default: page(1) size(9) sort(createAt)\
                Param boxType(SEND RECEIVE ALL)
                """
    )
    @GetMapping
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponse<LetterSummaryResponse>> getLetters(
            @RequestParam(value = "boxType", required = false) String boxType,
            @Valid CommonPageRequest commonPageRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(PageResponse.from(
                letterBoxFacade.getLetters(userDetails.getUserId(), boxType, commonPageRequest.toCommand())));
    }

    @Operation(summary = "보관된 편지 삭제", description = "편지ID, 편지타입(LETTER, REPLY_LETTER), 송수신 타입(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteLetterInBox(@RequestBody @Valid List<LetterDeleteRequest> letterDeleteRequests,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        letterBoxFacade.deleteLetters(userDetails.getUserId(), LetterDeleteRequest.toCommandList(letterDeleteRequests));
        return ApiResponse.onSuccess("보관된 편지를 삭제했습니다.");
    }

    @Operation(summary = "보관된 편지 삭제", description = "송수신 타입(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다.")
    @DeleteMapping("/all")
    public ApiResponse<String> deleteLettersInBox(
            @RequestParam(value = "boxType", required = false) String boxType,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        letterBoxFacade.deleteAllLetters(userDetails.getUserId(), boxType);
        return ApiResponse.onSuccess("보관된 편지를 모두 삭제했습니다");
    }
}
