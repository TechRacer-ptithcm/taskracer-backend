package ptithcm.itmc.taskracer.controller.process.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.common.web.response.ResponseMessage;
import ptithcm.itmc.taskracer.controller.dto.auth.*;
import ptithcm.itmc.taskracer.controller.mapper.auth.AuthControllerMapper;
import ptithcm.itmc.taskracer.exception.MissingFieldException;
import ptithcm.itmc.taskracer.service.process.auth.AuthService;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthControllerMapper authControllerMapper;

    @PostMapping("sign-up")
    public ResponseEntity<ResponseAPI<?>> createNewUser(@Valid @RequestBody SignUpRequest request) throws MessagingException {
        log.info("Create new user: {}", request);
        if (request.getUsername().isEmpty() || request.getPassword().isEmpty() || request.getEmail().isEmpty())
            throw new MissingFieldException("Missing field.");
        var data = authService.createNewUser(authControllerMapper.toSignUpDto(request));
        var result = authControllerMapper.toSignUpResponse(data);
        var response = ResponseAPI.<SignUpResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(result)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("sign-in")
    public ResponseEntity<ResponseAPI<?>> signIn(@RequestBody SignInRequest request) {
        if (request.getInputAccount().isEmpty() || request.getPassword().isEmpty())
            throw new MissingFieldException("Missing field.");
        var data = authService.signIn(authControllerMapper.toSignInDto(request));
        var result = authControllerMapper.toSignInResponse(data);
        var response = ResponseAPI.<SignInResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(result)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("verify-account")
    public ResponseEntity<ResponseAPI<?>> verifyAccount(@RequestBody VerifyAccountRequest request) {
//        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        var userData = ParseObject.parse(principal, UserDto.class);
        authService.verifyAccount(request.getOtp());
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Active account successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("send-otp-forgot-password")
    public ResponseEntity<ResponseAPI<?>> forgotPassword(@RequestBody ForgotOtpRequest request) throws MessagingException {
        authService.sendOtpForgotPassword(request.getAccount());
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Send OTP to email successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("verify-otp-forgot-password")
    public ResponseEntity<ResponseAPI<?>> verifyForgotPasswordOtp(@RequestBody OtpForgotPasswordRequest request) throws Exception {
        var data = authService.VerifyChangePassword(request.getOtp());
        var returnData = OtpForgotPasswordResponse.builder()
                .token(data.getPrivateToken())
                .build();
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(returnData)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("resend-email")
    public ResponseEntity<ResponseAPI<?>> resendOtp(@RequestBody ResendOtpRequest request) throws MessagingException {
        authService.resendOtp(request.getAccount());
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Resend OTP successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("reset-password")
    public ResponseEntity<ResponseAPI<?>> changePassword(@RequestBody ChangePasswordRequest request) throws Exception {
        authService.changePassword(request.getToken(), request.getNewPassword());
        var response = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Change password successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
