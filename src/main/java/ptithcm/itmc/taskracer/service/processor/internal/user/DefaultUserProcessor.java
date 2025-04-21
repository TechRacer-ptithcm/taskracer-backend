package ptithcm.itmc.taskracer.service.processor.internal.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.processor.IUserProcessor;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-USER-PROCESSOR")
public class DefaultUserProcessor implements IUserProcessor {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;
    @Override
    public JpaUser edit(UserDto userData, UserDto ownerDto) {
        var mergeData = userServiceMapper.merge(ownerDto, userData);
        log.info("merge data: {}", mergeData);
        var dataUpdate = userServiceMapper.toJpa(mergeData);
        return jpaUserRepository.save(dataUpdate);
    }
}
