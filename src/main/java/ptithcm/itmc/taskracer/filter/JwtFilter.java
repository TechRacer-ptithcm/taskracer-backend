package ptithcm.itmc.taskracer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ptithcm.itmc.taskracer.exception.AuthenticationFailedException;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> BYPASS_PATHS = List.of(
            "/api/auth",
            "/api/docs",
            "/api/swagger-ui",
            "/api/api-docs"
    );

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }

    private void validateToken(String token) throws AuthenticationFailedException {
        if (!jwtUtil.validateToken(token)) {
            throw new AuthenticationFailedException("Invalid JWT token");
        }
        if (jwtUtil.isTokenExpired(token)) {
            throw new AuthenticationFailedException("Expired JWT token");
        }
    }

    private Optional<UserDto> extractUserFromToken(String token) {
        Map<String, Object> userDataRaw = jwtUtil.getClaim(token, "data");
        if (userDataRaw.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(objectMapper.convertValue(userDataRaw, UserDto.class));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // Bypass các path không cần xác thực
        if (BYPASS_PATHS.stream().anyMatch(requestUri::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token từ header Authorization
        String token = extractToken(request);
        if (token == null) {
            throw new AuthenticationFailedException("Missing Authorization Header");
        }

        try {
            validateToken(token);

            // Lấy thông tin user từ token
            Optional<UserDto> userOptional = extractUserFromToken(token);

            if (userOptional.isEmpty()) {
                throw new AuthenticationFailedException("Failed to set user authentication in security context");
            }

            UserDto user = userOptional.get();

            if (requestUri.startsWith("/api/admin") && !"ADMIN".equals(user.getTier().name())) {
                throw new AuthenticationFailedException("You don't have permission to access this endpoint");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }
}
