package ptithcm.itmc.taskracer.controller.mapper.auth;

import org.mapstruct.Mapper;
import ptithcm.itmc.taskracer.controller.dto.auth.SignInRequest;
import ptithcm.itmc.taskracer.controller.dto.auth.SignInResponse;
import ptithcm.itmc.taskracer.controller.dto.auth.SignUpRequest;
import ptithcm.itmc.taskracer.controller.dto.auth.SignUpResponse;
import ptithcm.itmc.taskracer.service.dto.auth.SignInRequestDto;
import ptithcm.itmc.taskracer.service.dto.auth.SignInResponseDto;
import ptithcm.itmc.taskracer.service.dto.auth.SignUpRequestDto;
import ptithcm.itmc.taskracer.service.dto.auth.SignUpResponseDto;

@Mapper(componentModel = "spring")
public interface AuthControllerMapper {
    SignUpRequestDto toSignUpDto(SignUpRequest request);

    SignUpResponse toSignUpResponse(SignUpResponseDto response);

    SignInRequestDto toSignInDto(SignInRequest request);

    SignInResponse toSignInResponse(SignInResponseDto response);


}
