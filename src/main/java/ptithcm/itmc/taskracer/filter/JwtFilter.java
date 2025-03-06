package ptithcm.itmc.taskracer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.service.process.user.UserService;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestUri = request.getRequestURI();
        log.info("Request URI: {}", requestUri);
        String[] bypassPaths = {
                "/api/auth",
                "/api/docs",
                "/api/swagger-ui",
                "/api/api-docs"};
        for (String bypassPath : bypassPaths) {
            if (requestUri.startsWith(bypassPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

//        if (requestUri.startsWith("/api/auth/") || requestUri.startsWith("/api/docs")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), ResponseAPI.builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .data(new ErrorObject("Missing Authorization Header"))
                    .build());
            return;
        }
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            if (!jwtUtil.validateToken(token)) {
                throw new AuthenticationException("Invalid JWT token");
            }
            if (jwtUtil.isTokenExpired(token)) {
                throw new AuthenticationException("Expired JWT token");
            }
            String username = jwtUtil.getClaim(token, "username");
            if (username != null) {
                var user = userService.getUser(username);
                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            } else {
//                throw new InternalError("Failed to set user authentication in security context");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                objectMapper.writeValue(response.getWriter(), ResponseAPI.builder()
                        .code(ResponseCode.MISSING_FIELD.getCode())
                        .message(ResponseCode.MISSING_FIELD.getMessage())
                        .status(false)
                        .data(new ErrorObject("Failed to set user authentication in security context"))
                        .build());
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), ResponseAPI.builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build());
            return;
        }
//        filterChain.doFilter(request, response);
    }
}
