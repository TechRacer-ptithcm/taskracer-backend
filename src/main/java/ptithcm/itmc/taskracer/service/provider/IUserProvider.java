package ptithcm.itmc.taskracer.service.provider;

import org.springframework.data.domain.Pageable;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.model.JpaUser;

import java.util.List;

public interface IUserProvider {
    JpaUser getUserDataByUserName(String username);

    PageableObject<List<JpaUser>> getAllUser(Pageable pageable);
}
