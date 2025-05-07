package ptithcm.itmc.taskracer.service.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.common.file.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamContentDto {
    private UUID id;

    private UUID userId;

    private Integer teamId;

    private String content;

    private List<FileInfo> fileAttachmentUrl;

    private Integer likeCount;
}
