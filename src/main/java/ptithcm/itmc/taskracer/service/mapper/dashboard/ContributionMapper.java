package ptithcm.itmc.taskracer.service.mapper.dashboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.repository.model.JpaContribution;
import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;
import ptithcm.itmc.taskracer.service.mapper.user.UserServiceMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserServiceMapper.class})
public interface ContributionMapper {
    ContributionDto toDto(JpaContribution request);

    default List<ContributionDto> toDto(List<JpaContribution> request) {
        return request.stream()
                .map(this::toDto)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    JpaContribution toJpa(ContributionDto dto);
}
