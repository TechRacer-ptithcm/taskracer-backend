package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.service.dto.user.UserDto;

import java.util.Optional;

public interface IEmailProvider {
    Optional<UserDto> getUserFromOtp(String otp) throws Exception;
    
}
