package ptithcm.itmc.taskracer.service.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaOtp;
import ptithcm.itmc.taskracer.service.dto.auth.OtpCodeDto;
import ptithcm.itmc.taskracer.service.dto.auth.SignUpResponseDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface AuthServiceMapper {

    @Mapping(target = "user.id", source = "userId")
    JpaOtp toJpaOtp(OtpCodeDto otp);

    SignUpResponseDto toDto(UserDto request);
}
