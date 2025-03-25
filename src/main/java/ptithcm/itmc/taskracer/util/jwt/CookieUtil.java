package ptithcm.itmc.taskracer.util.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CookieUtil {
    public static void createHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setHttpOnly(true);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
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
            UUID userId,
            String username,
            int maxAgeSeconds) {
        int timeToLive = maxAgeSeconds * 24 * 60 * 60;
        String refreshToken = jwtUtil.generateToken(
                userId,
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
