package ptithcm.itmc.taskracer.service.provider.internal.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaUserRepository;
import ptithcm.itmc.taskracer.repository.model.JpaUser;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;
import ptithcm.itmc.taskracer.service.provider.IUserProvider;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-USER-PROVIDER")
public class DefaultUserProvider implements IUserProvider {
    private final JpaUserRepository jpaUserRepository;
    private final UserServiceMapper userServiceMapper;

    @Override
    public JpaUser getUserDataByUserName(String username) {
        var data = jpaUserRepository.findByUsername(username);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        return data.get();
    }

    @Override
    public JpaUser getUserDataByUserId(UUID userId) {
        var data = jpaUserRepository.findById(userId);
        if (data.isEmpty()) {
            throw new ResourceNotFound("User not found.");
        }
        return data.get();
    }

    @Override
    public PageableObject<List<JpaUser>> getAllUser(Pageable pageable) {
        var data = jpaUserRepository.findAll(pageable);
        return PageableObject.<List<JpaUser>>builder()
                .content(data.getContent())
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPages())
                .currentPage(data.getNumber())
                .build();
    }
}
