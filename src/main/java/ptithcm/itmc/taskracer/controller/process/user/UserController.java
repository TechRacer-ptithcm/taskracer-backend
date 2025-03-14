package ptithcm.itmc.taskracer.controller.process.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.common.web.response.ResponseMessage;
import ptithcm.itmc.taskracer.controller.dto.auth.ChangePasswordRequest;
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
    }

    @PostMapping("change-password")
    public ResponseEntity<ResponseAPI<?>> changePassword(@RequestBody ChangePasswordRequest request) throws Exception {
        userService.changePassword(request.getToken(), request.getNewPassword());
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Change password successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
}
