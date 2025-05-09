package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.processor.IUserProcessor;
import ptithcm.itmc.taskracer.service.provider.IUserProvider;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-USER")
public class UserService {
    private final IUserProcessor processor;
    private final IUserProvider provider;
    private final UserServiceMapper mapper;

    @Cacheable(value = "user", key = "#p0")
    public UserDto getUserDataByUserName(String username) {
        log.info("Get user data by username: {}", username);
        var data = provider.getUserDataByUserName(username);
        return mapper.toDto(data);
    }

    public UserDto getUserDataById(UUID userId) {
        log.info("Get user data by id: {}", userId);
        var data = provider.getUserDataByUserId(userId);
        return mapper.toDto(data);
    }

    public PageableObject<List<UserDto>> getAllUser(Pageable pageable) {
        var data = provider.getAllUser(pageable);
        var result = mapper.toDto(data.getContent());
        return PageableObject.<List<UserDto>>builder()
                .content(result)
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPage())
                .currentPage(data.getCurrentPage())
                .build();
    }

    @CachePut(value = "user", key = "#p1.username")
    public UserDto editUser(UserDto userData, UserDto ownerDto) {
        var data = processor.edit(userData, ownerDto);
        return mapper.toDto(data);
    }
}
