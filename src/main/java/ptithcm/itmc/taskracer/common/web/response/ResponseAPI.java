package ptithcm.itmc.taskracer.common.web.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAPI<T> {
    private String message;
    private String code;
    private boolean status;
    private T data;
}
