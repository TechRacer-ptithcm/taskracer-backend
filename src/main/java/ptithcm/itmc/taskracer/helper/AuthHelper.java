package ptithcm.itmc.taskracer.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.process.user.UserService;
import ptithcm.itmc.taskracer.util.json.ParseObject;

@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final UserService userService;

    public UserDto getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userData = ParseObject.parse(principal, UserDto.class);
        return userService.getUser(userData.getUsername());
    }
}
