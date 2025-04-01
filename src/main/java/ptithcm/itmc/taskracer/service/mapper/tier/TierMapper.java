package ptithcm.itmc.taskracer.service.mapper.tier;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ptithcm.itmc.taskracer.repository.model.JpaTier;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;
import ptithcm.itmc.taskracer.service.dto.tier.TierDto;

@Mapper(componentModel = "spring")
public interface TierMapper {
    TierMapper INSTANCE = Mappers.getMapper(TierMapper.class);

    JpaTier toJpa(TierDto tier);

    TierDto toDto(JpaTier tier);

    @Named("mapTier")
    default Tier map(JpaTier tier) {
        if (tier == null)
            return null;
        return Tier.valueOf(tier.getName().name());
    }

    default JpaTier map(Tier tier) {
        return new JpaTier(tier.ordinal(), tier, "");
    }
}
