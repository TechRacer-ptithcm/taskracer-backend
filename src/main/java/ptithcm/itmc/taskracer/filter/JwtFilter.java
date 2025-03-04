package ptithcm.itmc.taskracer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
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
            String username = jwtUtil.extractUsername(token);
            if (username != null) {
                var user = userService.getUser(username);
                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                throw new InternalError("Failed to set user authentication in security context");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), ResponseAPI.builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .data(new ErrorObject("Missing Authorization Header"))
                    .build());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
