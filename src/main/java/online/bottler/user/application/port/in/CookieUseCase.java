package online.bottler.user.application.port.in;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieUseCase {
    void addCookie(HttpServletResponse response, String name, String value);
}
