package ptithcm.itmc.taskracer.common.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PageableObject<T> {
    private T content;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("total_elements")
    private Long totalElements;
}
