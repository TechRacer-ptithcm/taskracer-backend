package ptithcm.itmc.taskracer.service.mapper.tier;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ptithcm.itmc.taskracer.repository.model.JpaTier;
import ptithcm.itmc.taskracer.service.dto.tier.TierDto;

@Mapper(componentModel = "spring")
public interface TierMapper {
    TierMapper INSTANCE = Mappers.getMapper(TierMapper.class);

    JpaTier toJpaTier(TierDto tier);

    TierDto toTierDto(JpaTier tier);
}
