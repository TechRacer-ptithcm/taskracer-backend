package ptithcm.itmc.taskracer.controller.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.mapper.user.UserControllerMapper;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.process.user.UserService;
import ptithcm.itmc.taskracer.util.json.ParseObject;

@RestController
@Controller
@RequestMapping("social")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserControllerMapper userControllerMapper;

    @GetMapping("user")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseAPI<?>> getUserInfo() {
        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var userData = ParseObject.parse(principal, UserDto.class);
            var data = userService.getUser(userData.getUsername());
            var result = ResponseAPI.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .status(true)
                    .data(userControllerMapper.toUserResponse(data))
                    .build();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            var result = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
