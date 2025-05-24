package online.bottler.user.application.port.in;

import java.util.Map;

public interface KakaoUseCase {
    String getRequestURL();
    String getKakaoAccessToken(String code);
    Map<String, String> getUserInfo(String accessToken);
}
