package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.util.Optional;

public interface IEmailProvider {
    Optional<JpaUser> getUserFromOtp(String otp) throws Exception;
    
}
