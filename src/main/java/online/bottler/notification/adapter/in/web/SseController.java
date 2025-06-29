package online.bottler.notification.adapter.in.web;

import lombok.RequiredArgsConstructor;
import online.bottler.auth.CustomUserDetails;
import online.bottler.notification.application.SseService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return sseService.connect(customUserDetails.getUserId().toString());
    }
}
