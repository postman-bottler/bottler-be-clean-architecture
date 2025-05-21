package online.bottler.mapletter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import online.bottler.global.response.ApiResponse;
import online.bottler.mapletter.application.dto.PaperDTO;
import online.bottler.mapletter.application.PaperService;

@RestController
@RequestMapping("/paper")
@RequiredArgsConstructor
public class PaperController {
    private final PaperService paperService;

    @GetMapping
    @Operation(summary = "", description = "")
    public ApiResponse<List<PaperDTO>> findPapers() {
        return ApiResponse.onSuccess(paperService.findPapers());
    }
}
