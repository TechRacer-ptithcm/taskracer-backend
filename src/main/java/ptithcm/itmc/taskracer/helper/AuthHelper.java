package ptithcm.itmc.taskracer.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.process.user.UserService;
import ptithcm.itmc.taskracer.util.json.ParseObject;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final UserService userService;

    public UserDto getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("principal: {}", principal);
        var userData = ParseObject.parse(principal, UserDto.class);
        return userService.getUser(userData.getUsername());
    }
}
