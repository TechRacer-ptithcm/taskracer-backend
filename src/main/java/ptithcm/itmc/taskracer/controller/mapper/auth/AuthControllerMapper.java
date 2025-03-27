package ptithcm.itmc.taskracer.controller.mapper.auth;

import org.mapstruct.Mapper;
import ptithcm.itmc.taskracer.controller.dto.auth.*;
import ptithcm.itmc.taskracer.service.dto.auth.*;

@Mapper(componentModel = "spring")
public interface AuthControllerMapper {
    SignUpRequestDto toDto(SignUpRequest request);

    SignUpResponse toDomain(SignUpResponseDto response);

    SignInRequestDto toDto(SignInRequest request);

    SignInResponse toDomain(SignInResponseDto response);

    VerifyAccountResponse toDomain(VerifyAccountDto request);


}
