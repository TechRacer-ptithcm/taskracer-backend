package ptithcm.itmc.taskracer.controller.process.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.dto.user.UpdateUserRequest;
import ptithcm.itmc.taskracer.controller.mapper.user.UserControllerMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.process.user.UserService;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserControllerMapper userControllerMapper;
    private final AuthHelper authHelper;

    @GetMapping("user-data")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseAPI<?>> getUserInfo() {
//        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        var userData = ParseObject.parse(principal, UserDto.class);
//        var data = userService.getUser(userData.getUsername());
        var getUserData = authHelper.getUser();
        var data = userService.getUserDataByUserName(getUserData.getUsername());
        var result = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(userControllerMapper.toUserResponse(data))
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("users")
    public ResponseEntity<ResponseAPI<?>> getAllUser(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        var data = userService.getAllUser(pageable);
        var result = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(data)
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("user")
    public ResponseEntity<ResponseAPI<?>> getUser(@RequestParam(value = "username") String username) {
        var data = userService.getUserDataByUserName(username);
        var result = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(userControllerMapper.toUserResponse(data))
                .build();
        return ResponseEntity.ok(result);
    }

    @PutMapping("user")
    public ResponseEntity<ResponseAPI<?>> updateUser(@RequestBody UpdateUserRequest request) {
        var userData = authHelper.getUser();
        var data = userService.editUser(userControllerMapper.toUserDto(request), userData);
        var result = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(userControllerMapper.toUserResponse(data))
                .build();
        return ResponseEntity.ok(result);
    }
}
