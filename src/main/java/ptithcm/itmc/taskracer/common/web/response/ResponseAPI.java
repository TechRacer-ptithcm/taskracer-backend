package ptithcm.itmc.taskracer.common.web.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAPI<T> {
    private String message;
    private String code;
    private boolean status;
    private T data;
    @JsonProperty("stack_trace")
    private StackTraceElement[] stackTrace;
}
