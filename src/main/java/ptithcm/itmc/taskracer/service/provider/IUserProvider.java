package ptithcm.itmc.taskracer.service.provider;

import org.springframework.data.domain.Pageable;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.util.List;
import java.util.UUID;

public interface IUserProvider {
    JpaUser getUserDataByUserName(String username);

    JpaUser getUserDataByUserId(UUID userId);

    PageableObject<List<JpaUser>> getAllUser(Pageable pageable);
}
