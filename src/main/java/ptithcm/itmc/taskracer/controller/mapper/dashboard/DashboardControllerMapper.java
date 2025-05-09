package ptithcm.itmc.taskracer.controller.mapper.dashboard;

import org.mapstruct.Mapper;
import ptithcm.itmc.taskracer.controller.dto.dashboard.ContributionResponse;
import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardControllerMapper {
    ContributionResponse toDomain(ContributionDto request);

    default List<ContributionResponse> toDomain(List<ContributionDto> request) {
        return request.stream()
                .map(this::toDomain)
                .toList();
    }
}
