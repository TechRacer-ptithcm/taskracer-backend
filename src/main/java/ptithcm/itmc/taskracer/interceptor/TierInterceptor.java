package ptithcm.itmc.taskracer.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.util.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
public class TierInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String getToken = request.getHeader("Authorization").substring(7);
        var getRole = jwtUtil.getClaim(getToken, "tier");
        ObjectMapper objectMapper = new ObjectMapper();
        request.setAttribute("username", jwtUtil.getClaim(getToken, "username"));
        if (getRole.equals("ADMIN")) {
            return true;
        } else {
            objectMapper.writeValue(response.getWriter(), ResponseAPI.builder()
                    .code(ResponseCode.TIER_INSUFFICIENT.getCode())
                    .message(ResponseCode.TIER_INSUFFICIENT.getMessage())
                    .status(false)
                    .data(new ErrorObject("Tier insufficient"))
                    .build());
            return false;
        }
    }
}