package ptithcm.itmc.taskracer.common.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseMessage {
    private String message;
}
