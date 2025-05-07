package ptithcm.itmc.taskracer.controller.process.team;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.common.web.response.ResponseMessage;
import ptithcm.itmc.taskracer.controller.dto.team.GetTeamContentResponse;
import ptithcm.itmc.taskracer.controller.dto.team.TeamContentRequest;
import ptithcm.itmc.taskracer.controller.mapper.team.TeamControllerMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
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
    private final AuthHelper helper;

    @GetMapping("team/{teamId}/post/{contentId}")
    public ResponseEntity<ResponseAPI<?>> getById(@PathVariable String teamId, @PathVariable UUID contentId) {
        var userData = helper.getUser();
        var data = service.getById(Integer.parseInt(teamId), contentId, userData.getId());
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.<GetTeamContentResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(formatData)
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("team/{teamId}")
    public ResponseEntity<ResponseAPI<?>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String teamId) {
        Pageable pageable = PageRequest.of(page, size);
        var userData = helper.getUser();
        var data = service.getAll(pageable,Integer.parseInt(teamId), userData.getId());
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(formatData)
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "team/{teamId}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseAPI<?>> create(
            @PathVariable String teamId,
            @RequestPart(value = "content") TeamContentRequest contentRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
            ) throws IOException {
        var userData = helper.getUser();
        var data = service.create(Integer.parseInt(teamId), mapper.toDto(contentRequest), userData.getId(), files);
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.<GetTeamContentResponse>builder()
               .code(ResponseCode.SUCCESS.getCode())
               .message(ResponseCode.SUCCESS.getMessage())
              .data(formatData)
              .status(true)
              .build();
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = "team/{teamId}/post/{contentId}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseAPI<?>> update(
            @PathVariable String teamId,
            @PathVariable UUID contentId,
            @RequestPart(value = "content") TeamContentRequest contentRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) throws IOException {
        var userData = helper.getUser();
        var data = service.update(Integer.parseInt(teamId), contentId, mapper.toDto(contentRequest), userData.getId(), files);
        var formatData = mapper.toDomain(data);
        var resp = ResponseAPI.<GetTeamContentResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(formatData)
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("team/{teamId}/post/{contentId}")
    public ResponseEntity<ResponseAPI<?>> delete(@PathVariable String teamId, @PathVariable UUID contentId) {
        var userData = helper.getUser();
        service.delete(Integer.parseInt(teamId), contentId, userData.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(new ResponseMessage("Delete post successful"))
                .status(true)
                .build();
        return ResponseEntity.ok(resp);
    }
}
