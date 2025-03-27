package ptithcm.itmc.taskracer.service.process.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.List;

public interface IUserService {
    UserDto getUserDataByUserName(String username);

    PageableObject<List<UserDto>> getAllUser(Pageable pageable);

    UserDto editUser(UserDto userData, UserDto ownerDto);
}

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceProcessor implements IUserService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    @Override
    @Cacheable(value = "user", key = "#p0")
    public UserDto getUserDataByUserName(String username) {
        var data = jpaUserRepository.findByUsername(username);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        return userServiceMapper.toDto(data.get());
    }

    @Override
    public PageableObject<List<UserDto>> getAllUser(Pageable pageable) {
        var data = jpaUserRepository.findAll(pageable);
        return PageableObject.<List<UserDto>>builder()
                .content(userServiceMapper.toDto(data.getContent()))
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .currentPage(data.getNumber())
                .build();
    }

    @Override
    @CachePut(value = "user", key = "#p1.username")
    public UserDto editUser(UserDto userData, UserDto ownerDto) {
        var mergeData = userServiceMapper.merge(ownerDto, userData);
        log.info("merge data: {}", mergeData);
        var dataUpdate = userServiceMapper.toJpa(mergeData);
        var saveData = jpaUserRepository.save(dataUpdate);
        return userServiceMapper.toDto(saveData);
    }
}
