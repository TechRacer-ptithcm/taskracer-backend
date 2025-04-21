package ptithcm.itmc.taskracer.service.processor;

import jakarta.mail.MessagingException;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

public interface IEmailProcessor {
    void sendOtp(UserDto userData) throws MessagingException;
}
