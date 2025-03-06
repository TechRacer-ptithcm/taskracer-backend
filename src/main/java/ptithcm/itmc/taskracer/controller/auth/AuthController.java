package ptithcm.itmc.taskracer.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.common.web.response.ResponseMessage;
import ptithcm.itmc.taskracer.controller.dto.auth.*;
import ptithcm.itmc.taskracer.controller.mapper.auth.AuthControllerMapper;
import ptithcm.itmc.taskracer.exception.*;
import ptithcm.itmc.taskracer.service.process.auth.AuthService;


@RestController
@Controller
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthControllerMapper authControllerMapper;

    @PostMapping("sign-up")
    public ResponseEntity<ResponseAPI<?>> createNewUser(@RequestBody SignUpRequest request) {
        try {
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
        } catch (ValidationFailedException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.VALIDATE_FAILED.getCode())
                    .message(ResponseCode.VALIDATE_FAILED.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (MissingFieldException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DuplicateDataException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.DUPLICATE_DATA.getCode())
                    .message(ResponseCode.DUPLICATE_DATA.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("sign-in")
    public ResponseEntity<ResponseAPI<?>> signIn(@RequestBody SignInRequest request) {
        try {
            log.info("Sign In: {}", request);
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
        } catch (ValidationFailedException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.VALIDATE_FAILED.getCode())
                    .message(ResponseCode.VALIDATE_FAILED.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (MissingFieldException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DuplicateDataException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.DUPLICATE_DATA.getCode())
                    .message(ResponseCode.DUPLICATE_DATA.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("verify-account")
    public ResponseEntity<ResponseAPI<?>> verifyAccount(@RequestBody VerifyAccountRequest request) {
//        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        var userData = ParseObject.parse(principal, UserDto.class);
        try {
            authService.verifyAccount(request.getOtp());
            var response = ResponseAPI.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .status(true)
                    .data(new ResponseMessage("Active account successful"))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (MissingFieldException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (ExpiredException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.EXPIRED_CODE.getCode())
                    .message(ResponseCode.EXPIRED_CODE.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PostMapping("send-otp-forgot-password")
    public ResponseEntity<ResponseAPI<?>> forgotPassword(@RequestBody ForgotOtpRequest request) {
        try {
            authService.sendOtpForgotPassword(request.getAccount());
            var response = ResponseAPI.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .status(true)
                    .data(new ResponseMessage("Send OTP to email successful"))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (MissingFieldException e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("verify-otp-forgot-password")
    private ResponseEntity<ResponseAPI<?>> verifyForgotPasswordOtp(@RequestBody OtpForgotPasswordRequest request) {
        try {
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
        } catch (ResourceNotFound e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.RESOURCE_NOT_FOUND.getCode())
                    .message(ResponseCode.RESOURCE_NOT_FOUND.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("change-password")
    private ResponseEntity<ResponseAPI<?>> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            authService.changePassword(request.getToken(), request.getNewPassword());
            var response = ResponseAPI.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .status(true)
                    .data(new ResponseMessage("Change password successful"))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.<ErrorObject>builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
