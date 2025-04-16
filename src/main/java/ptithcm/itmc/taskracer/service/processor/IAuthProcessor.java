package ptithcm.itmc.taskracer.service.processor;

import jakarta.mail.MessagingException;
import ptithcm.itmc.taskracer.service.dto.auth.*;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

public interface IAuthProcessor {
    SignUpResponseDto createNewUser(SignUpRequestDto request) throws MessagingException;

    SignInResponseDto signIn(SignInRequestDto request, UserDto user);

    VerifyAccountDto verifyAccount(String username);

    void sendOtpForgotPassword(UserDto user) throws MessagingException;

    OtpForgotPasswordDto VerifyChangePassword(UserDto user) throws Exception;

    void resendOtp(UserDto user) throws MessagingException;

    void changePassword(String token, String newPassword) throws Exception;

    String refreshAccessToken(String token);
}
