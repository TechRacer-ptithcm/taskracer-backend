package ptithcm.itmc.taskracer.service.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaOtp;
import ptithcm.itmc.taskracer.service.dto.auth.OtpCodeDto;

@Mapper(componentModel = "spring")
public interface AuthServiceMapper {

    @Mapping(target = "user.id", source = "userId")
    JpaOtp toJpaOtp(OtpCodeDto otp);
}
