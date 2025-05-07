package ptithcm.itmc.taskracer.service.processor;

import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;

import java.util.UUID;

public interface ITeamContentProcessor {
   JpaTeamContent save(TeamContentDto content);

   JpaTeamContent update(TeamContentDto content);

   void delete(UUID contentId);
}
