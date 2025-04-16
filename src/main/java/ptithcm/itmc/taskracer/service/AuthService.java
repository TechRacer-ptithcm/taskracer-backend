package ptithcm.itmc.taskracer.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.exception.ExpiredException;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.auth.*;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.processor.IAuthProcessor;
import ptithcm.itmc.taskracer.service.provider.IEmailProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JpaUserRepository jpaUserRepository;
    private final IAuthProcessor processor;
    private final UserServiceMapper userServiceMapper;
    private final IEmailProvider emailProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Transactional
    public SignUpResponseDto createNewUser(SignUpRequestDto request) throws MessagingException {
        if (jpaUserRepository.findByUsername(request.getUsername()).isPresent() ||
                jpaUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateDataException("Username or email already exists.");
        }
        return processor.createNewUser(request);
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        var user = jpaUserRepository.findByUsername(request.getInputAccount())
                .or(() -> jpaUserRepository.findByEmail(request.getInputAccount()))
                .orElseThrow(() -> new ResourceNotFound("User not found."));
        return processor.signIn(request, userServiceMapper.toDto(user));
    }

    @Transactional
    public VerifyAccountDto verifyAccount(String otp) {
        String key = "otp:" + otp;
        if (!redisTemplate.hasKey(key)) throw new ExpiredException("OTP is not found or already used.");
        String getUsername = (String) (redisTemplate.opsForValue().getAndDelete(key));
        return processor.verifyAccount(getUsername);
    }

    @Transactional
    public void sendOtpForgotPassword(String account) throws MessagingException {
        var user = jpaUserRepository.findByEmail(account)
                .or(() -> jpaUserRepository.findByUsername(account))
                .orElseThrow(() -> new ResourceNotFound("Email or username not found."));
        processor.sendOtpForgotPassword(userServiceMapper.toDto(user));
    }

    public OtpForgotPasswordDto VerifyChangePassword(String otp) throws Exception {
        var userData = emailProvider.getUserFromOtp(otp).orElseThrow(() -> new ExpiredException("OTP is not found or already used."));
        return processor.VerifyChangePassword(userData);
    }

    public void resendOtp(String account) throws MessagingException {
        var user = jpaUserRepository.findByEmail(account)
                .or(() -> jpaUserRepository.findByUsername(account))
                .orElseThrow(() -> new ResourceNotFound("Email or username not found."));
        processor.resendOtp(userServiceMapper.toDto(user));
    }

    @Transactional
    public void changePassword(String token, String newPassword) throws Exception {
        processor.changePassword(token, newPassword);
    }

    public String refreshAccessToken(String token) {
        return processor.refreshAccessToken(token);
    }
}