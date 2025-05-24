package online.bottler.letter.v2.adapter.in.web;

import static online.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;
import static online.bottler.global.response.code.ErrorStatus.REPLY_LETTER_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.global.response.ApiResponse;
import online.bottler.letter.application.LetterDeletionService;
import online.bottler.letter.application.dto.LetterDeleteDTO;
import online.bottler.letter.presentation.annotation.LetterValidationMetaData;
import online.bottler.letter.v2.adapter.in.web.request.ReplyLetterDeleteRequest;
import online.bottler.letter.v2.adapter.in.web.request.ReplyLetterRequest;
import online.bottler.letter.v2.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.v2.application.port.in.CreateReplyLetterUseCase;
import online.bottler.letter.v2.application.port.in.GetReplyLetterDetailUseCase;
import online.bottler.letter.v2.application.port.in.GetReplyLetterSummariesUseCase;
import online.bottler.letter.v2.application.response.PageResponse;
import online.bottler.letter.v2.application.response.ReplyLetterDetailResponse;
import online.bottler.letter.v2.application.response.ReplyLetterResponse;
import online.bottler.letter.v2.application.response.ReplyLetterSummaryResponse;
import online.bottler.user.adapter.in.web.auth.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/letters/replies")
@RequiredArgsConstructor
@Tag(name = "Reply Letters", description = "키워드 편지 API")
public class ReplyLetterController {

    private final CreateReplyLetterUseCase createReplyLetterUseCase;
    private final GetReplyLetterSummariesUseCase getReplyLetterSummariesUseCase;
    private final LetterDeletionService letterDeletionService;
    private final GetReplyLetterDetailUseCase getReplyLetterDetailUseCase;

    @Operation(summary = "키워드 편지 생성", description = "지정된 편지 ID에 대한 답장을 생성합니다.")
    @PostMapping("/{letterId}")
    @LetterValidationMetaData(message = "키워드 답장 편지 유효성 검사 실패", errorStatus = REPLY_LETTER_VALIDATION_ERROR)
    public ApiResponse<ReplyLetterResponse> createReplyLetter(@PathVariable Long letterId,
                                                              @RequestBody @Valid ReplyLetterRequest replyLetterRequest,
                                                              BindingResult bindingResult,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onCreateSuccess(
                createReplyLetterUseCase.create(replyLetterRequest.toCommand(letterId, userDetails.getUserId())));
    }

    @Operation(summary = "특정 키워드 편지에 대한 답장 목록 조회", description = "지정된 편지 ID에 대한 답장들의 제목, 라벨이미지, 작성날짜를 페이지네이션 형태로 반환합니다."
            + "\nPage Default: page(1) size(9) sort(createAt)")
    @GetMapping("/{letterId}")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponse<ReplyLetterSummaryResponse>> getRepliesForLetter(@PathVariable Long letterId,
                                                                                     @Valid PageRequest pageRequest,
                                                                                     BindingResult bindingResult,
                                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
//        letterBoxService.validateLetterInUserBox(userId, letterId); 서비스 내부로 이동

        Page<ReplyLetterSummaryResponse> result = getReplyLetterSummariesUseCase.getSummaries(
                ReplyLetterSummariesQuery.of(letterId, pageRequest, userId));
        return ApiResponse.onSuccess(PageResponse.from(result));
    }

    @Operation(summary = "답장 편지 상세 조회", description = "지정된 답장 편지의 ID에 대한 상세 정보를 반환합니다.")
    @GetMapping("/detail/{replyLetterId}")
    public ApiResponse<ReplyLetterDetailResponse> getReplyLetter(@PathVariable Long replyLetterId,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
//        letterBoxService.validateLetterInUserBox(userId, replyLetterId); 서비스 내부로 이동
        return ApiResponse.onSuccess(getReplyLetterDetailUseCase.getDetail(replyLetterId, userId));
    }

    @Operation(summary = "답장 편지 삭제", description = "답장 편지ID, 송수신 타입(SEND, RECEIVE)을 기반으로 답장 편지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteReplyLetter(
            @RequestBody @Valid ReplyLetterDeleteRequest replyLetterDeleteRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LetterDeleteDTO letterDeleteDTO = LetterDeleteDTO.fromReplyLetter(replyLetterDeleteRequest);
        letterDeletionService.deleteLetter(letterDeleteDTO, userDetails.getUserId());
        return ApiResponse.onSuccess("답장 편지가 성공적으로 삭제되었습니다.");
    }
}
