package ptithcm.itmc.taskracer.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.AuthenticationFailedException;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.util.json.ParseObject;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-HELPER")
public class AuthHelper {
    public UserDto getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userData = ParseObject.parse(principal, UserDto.class);
        log.info("Get user: {}", userData);
        if (!userData.getActive()) {
            throw new AuthenticationFailedException("User is not active.");
        }
        return userData;
    }
}
