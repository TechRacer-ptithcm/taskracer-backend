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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    @Cacheable(value = "user", key = "#p0")
    public UserDto getUserDataByUserName(String username) {
        var data = jpaUserRepository.findByUsername(username);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        return userServiceMapper.toUserDto(data.get());
    }

    public PageableObject<List<UserDto>> getAllUser(Pageable pageable) {
        var data = jpaUserRepository.findAll(pageable);
        return PageableObject.<List<UserDto>>builder()
                .content(userServiceMapper.toListUserDto(data.getContent()))
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .currentPage(data.getNumber())
                .build();
    }

    @CachePut(value = "user", key = "#p1.username")
    public UserDto editUser(UserDto userData, UserDto ownerDto) {
        var mergeData = userServiceMapper.merge(ownerDto, userData);
        log.info("merge data: {}", mergeData);
        var dataUpdate = userServiceMapper.toJpaUser(mergeData);
        var saveData = jpaUserRepository.save(dataUpdate);
        return userServiceMapper.toUserDto(saveData);
    }
}
