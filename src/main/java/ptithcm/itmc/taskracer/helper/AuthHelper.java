package ptithcm.itmc.taskracer.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.exception.AuthenticationFailedException;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.util.json.ParseObject;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHelper {
    public UserDto getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userData = ParseObject.parse(principal, UserDto.class);
        if (!userData.getActive()) {
            throw new AuthenticationFailedException("User is not active.");
        }
        return userData;
    }
}
