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
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.exception.AuthenticationFailedException;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.IUserService;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final List<String> BYPASS_PATHS = List.of(
            "/api/auth",
            "/api/docs",
            "/api/swagger-ui",
            "/api/api-docs",
            "/api/actuator"
    );
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IUserService userService;

    private String extractToken(String authorizationHeader) {
        log.info("Authorization header: {}", authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }

    private void validateToken(String token) throws AuthenticationFailedException {
        if (jwtUtil.isTokenExpired(token)) {
            throw new AuthenticationFailedException("Expired JWT token");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new AuthenticationFailedException("Invalid JWT token");
        }
    }

    private Optional<UserDto> extractUserFromToken(String token) {
        String getUserName = jwtUtil.extractUsername(token);
        if (getUserName.isEmpty()) {
            return Optional.empty();
        }
        var data = userService.getUserDataByUserName(getUserName);
        return Optional.ofNullable(data);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestUri = request.getRequestURI();
            log.info(">>> URI: {}", requestUri);
            // Log ra header
            log.info(">>> Method: {}", request.getMethod());
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info(">>> Header: {} = {}", headerName, headerValue);
            }
            //Bypass OPTIONS
            if (request.getMethod().equals("OPTIONS")) {
                filterChain.doFilter(request, response);
                return;
            }
            // Bypass các path không cần xác thực
            if (BYPASS_PATHS.stream().anyMatch(requestUri::startsWith)) {
                filterChain.doFilter(request, response);
                return;
            }
            // Lấy token từ header Authorization
            String authorizationHeader = request.getHeader("authorization");
            String token = extractToken(authorizationHeader);
            if (token == null) {
                throw new AuthenticationFailedException("Missing Authorization Header");
            }

            validateToken(token);

            // Lấy thông tin user từ token
            Optional<UserDto> userOptional = extractUserFromToken(token);

            if (userOptional.isEmpty()) {
                throw new AuthenticationFailedException("Failed to set user authentication in security context");
            }

            UserDto user = userOptional.get();

            if (requestUri.startsWith("/api/admin") && !Tier.ADMIN.name().equals(user.getTier().name())) {
                throw new AuthenticationFailedException("You don't have permission to access this endpoint");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Authentication error: {}", ex.getMessage());

            String origin = request.getHeader("Origin");
            List<String> allowedOrigins = List.of("http://localhost:5173", "https://taskracer.id.vn");

            if (origin != null && allowedOrigins.contains(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            var errorBody = objectMapper.writeValueAsString(
                    ResponseAPI.builder()
                            .data(new ErrorObject(ex.getMessage()))
                            .code(ResponseCode.AUTHENTICATION_FAILED.getCode())
                            .message(ResponseCode.AUTHENTICATION_FAILED.getMessage())
                            .status(false)
                            .build()
            );
            response.getWriter().write(errorBody);
        }
    }
}
