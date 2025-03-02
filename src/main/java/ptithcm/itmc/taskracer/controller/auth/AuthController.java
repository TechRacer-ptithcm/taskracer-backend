package ptithcm.itmc.taskracer.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.dto.auth.SignUpRequest;
import ptithcm.itmc.taskracer.controller.dto.auth.SignUpResponse;
import ptithcm.itmc.taskracer.controller.mapper.auth.AuthControllerMapper;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.MissingFieldException;
import ptithcm.itmc.taskracer.exception.ValidationFailedException;
import ptithcm.itmc.taskracer.service.process.auth.AuthService;

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
            var response = ResponseAPI.<SignUpResponse>builder()
                    .code(ResponseCode.VALIDATE_FAILED.getCode())
                    .message(ResponseCode.VALIDATE_FAILED.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (MissingFieldException e) {
            var response = ResponseAPI.<SignUpResponse>builder()
                    .code(ResponseCode.MISSING_FIELD.getCode())
                    .message(ResponseCode.MISSING_FIELD.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DuplicateDataException e) {
            var response = ResponseAPI.<SignUpResponse>builder()
                    .code(ResponseCode.DUPLICATE_DATA.getCode())
                    .message(ResponseCode.DUPLICATE_DATA.getMessage())
                    .status(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            var response = ResponseAPI.builder()
                    .code(ResponseCode.ERROR.getCode())
                    .message(ResponseCode.ERROR.getMessage())
                    .status(false)
                    .data(new ErrorObject(e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
