package ptithcm.itmc.taskracer.controller.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.itmc.taskracer.common.file.FileResponse;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTeamContentResponse {
    private UUID id;

    private UUID userId;

    private Integer teamId;

    private String content;

    private List<FileResponse> fileAttachmentUrl;

    private Integer likeCount;
}
