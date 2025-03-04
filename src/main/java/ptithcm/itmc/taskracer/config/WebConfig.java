package ptithcm.itmc.taskracer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ptithcm.itmc.taskracer.interceptor.TierInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final TierInterceptor tierInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tierInterceptor)
                .addPathPatterns(List.of("/api/admin/**", "/api/system-info"))
                .excludePathPatterns(List.of(
                        "/api/auth/**",
                        "/api/user/**",
                        "/api/social/**",
                        "/api/ranking/**",
                        "/api/leaderboard/**",
                        "/api/content/**",
                        "/api/docs"
                ));
    }
}
