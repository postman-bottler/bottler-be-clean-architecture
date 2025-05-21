package online.bottler.reply.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import online.bottler.global.response.ApiResponse;
import online.bottler.reply.application.dto.response.ReplyResponseDTO;
import online.bottler.reply.application.ReplyService;
import online.bottler.user.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
@Tag(name = "최근 답장 3개 보여주는 컨트롤러")
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping
    public ApiResponse<List<ReplyResponseDTO>> findRecentReplyLetters(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.onSuccess(replyService.findRecentReplyLetters(customUserDetails.getUserId()));
    }
}
