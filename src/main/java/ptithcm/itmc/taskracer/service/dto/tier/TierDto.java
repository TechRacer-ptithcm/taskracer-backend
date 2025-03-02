package ptithcm.itmc.taskracer.service.dto.tier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.repository.model.enumeration.Tier;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TierDto {
    private Integer id;
    private Tier name;
    private String description;
}
