package online.bottler.mapletter.adaptor.in.web;

import io.swagger.v3.oas.annotations.Operation;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.AdaptorException;
import online.bottler.global.response.ApiResponse;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.user.auth.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapLetterProximityController {

    private final MapLetterUseCase mapLetterUseCase;

    @GetMapping("/{letterId}")
    @Operation(summary = "편지 상세 조회", description = "로그인 필수. 위경도 필수. 반경 15m 내 편지만 상세조회 가능. 내가 타겟인 편지와 퍼블릭 편지만 조회 가능. 나머지는 오류")
    public ApiResponse<OneLetterResponse> findOneMapLetter(@RequestParam String latitude,
                                                           @RequestParam String longitude,
                                                           @PathVariable Long letterId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new AdaptorException("지도에서 해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterUseCase.findOneMapLetter(letterId, userId, lat, lon));
    }

    @GetMapping
    @Operation(summary = "주변 편지 조회", description = "로그인 필수. 반경 500m 내 퍼블릭 편지, 나에게 타겟으로 온 편지 조회")
    public ApiResponse<List<FindNearbyLettersResponse>> findNearbyMapLetters(@RequestParam String latitude,
                                                                             @RequestParam String longitude,
                                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new AdaptorException("지도에서 해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterUseCase.findNearByMapLetters(lat, lon, userId));
    }
}
