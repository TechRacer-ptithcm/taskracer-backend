package ptithcm.itmc.taskracer.service.process.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.exception.DuplicateDataException;
import ptithcm.itmc.taskracer.repository.JpaTierRepository;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.JpaTier;
import ptithcm.itmc.taskracer.repository.model.enumeration.Gender;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.dto.auth.SignUpRequestDto;
import ptithcm.itmc.taskracer.service.dto.auth.SignUpResponseDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.auth.AuthServiceMapper;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaTierRepository jpaTierRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder passwordEncoder;
    private final TierMapper tierMapper;

    public SignUpResponseDto createNewUser(SignUpRequestDto request) {
        if (jpaUserRepository.findByUsername(request.getUsername()).isPresent() ||
                jpaUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateDataException("Username or email already exists.");
        }
        var user = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .tier(Tier.USER)
                .active(false)
                .gender(Gender.MALE)
                .score(0)
                .name("")
                .build();
        log.info("Create new user: {}", userServiceMapper.toJpaUserDto(user));
        var savedUser = jpaUserRepository.save(userServiceMapper.toJpaUserDto(user));
        return SignUpResponseDto.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .active(savedUser.getActive())
                .build();
    }
}
