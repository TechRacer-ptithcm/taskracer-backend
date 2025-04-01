package ptithcm.itmc.taskracer.controller.process.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.mapper.team.TeamControllerMapper;
import ptithcm.itmc.taskracer.service.process.team.ITeamService;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
public class TeamController {
    private final ITeamService teamService;
    private final TeamControllerMapper teamControllerMapper;

    @GetMapping("team/{slug}")
    public ResponseEntity<ResponseAPI<?>> getTeamById(@PathVariable String slug) {
        var team = teamService.getTeamBySlug(slug);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(teamControllerMapper.toDomain(team))
                .build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("teams")
    public ResponseEntity<ResponseAPI<?>> getAllTeam() {
        return null;
    }

    @PostMapping("team")
    public ResponseEntity<ResponseAPI<?>> createTeam() {
        return null;
    }

    @PutMapping("team/{id}")
    public ResponseEntity<ResponseAPI<?>> updateTeam(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("team/{id}")
    public ResponseEntity<ResponseAPI<?>> deleteTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/remove/{id}")
    public ResponseEntity<ResponseAPI<?>> removeUserFromTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/invite/{id}")
    public ResponseEntity<ResponseAPI<?>> addUserToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/ask/{id}")
    public ResponseEntity<ResponseAPI<?>> addTaskToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/accept/{id}")
    public ResponseEntity<ResponseAPI<?>> acceptTaskToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/reject/{id}")
    public ResponseEntity<ResponseAPI<?>> rejectTaskToTeam(@PathVariable String id) {
        return null;
    }
}
