package ptithcm.itmc.taskracer.controller.process.team;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.dto.team.CreateNewTeamContentRequest;
import ptithcm.itmc.taskracer.controller.dto.team.GetTeamContentResponse;
import ptithcm.itmc.taskracer.controller.mapper.team.TeamControllerMapper;
import ptithcm.itmc.taskracer.service.TeamContentService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("content")
@RequiredArgsConstructor
public class TeamContentController {
    private final TeamContentService service;
    private final TeamControllerMapper mapper;

    @GetMapping("team/{id}")
    public ResponseEntity<ResponseAPI<?>> getById(@PathVariable UUID id) {
        var data = service.getById(id);
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.<GetTeamContentResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(formatData)
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("teams")
    public ResponseEntity<ResponseAPI<?>> getAll(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        var data = service.getAll(pageable);
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(formatData)
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "team", consumes = "multipart/form-data")
    public ResponseEntity<ResponseAPI<?>> create(
            @RequestPart(value = "content") CreateNewTeamContentRequest contentRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
            ) throws IOException {
        var data = service.create(mapper.toDto(contentRequest), files);
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.<GetTeamContentResponse>builder()
               .code(ResponseCode.SUCCESS.getCode())
               .message(ResponseCode.SUCCESS.getMessage())
              .data(formatData)
              .status(true)
              .build();
        return ResponseEntity.ok(resp);
    }
}
