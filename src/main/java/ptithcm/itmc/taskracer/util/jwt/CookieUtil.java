package ptithcm.itmc.taskracer.util.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CookieUtil {
    public static void createHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(path)
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();
        log.info("createHttpOnlyCookie: {}", cookie);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        log.info("getCookieValue: {}", (Object) request.getCookies());
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public static void deleteCookie(HttpServletResponse response, String name, String path) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(path);
        cookie.setMaxAge(0); // xóa cookie
        response.addCookie(cookie);
    }

    public static void addRefreshTokenCookie(
            HttpServletResponse response,
            JwtUtil jwtUtil,
            String username,
            int maxAgeSeconds) {
        int timeToLive = maxAgeSeconds * 24 * 60 * 60;
        String refreshToken = jwtUtil.generateToken(
                username,
                TimeUnit.DAYS.toMillis(maxAgeSeconds)
        );

        createHttpOnlyCookie(
                response,
                "refresh_token",
                refreshToken,
                timeToLive,
                "/"
        );
    }
}
