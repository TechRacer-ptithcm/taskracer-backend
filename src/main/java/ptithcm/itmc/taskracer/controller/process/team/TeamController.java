package ptithcm.itmc.taskracer.controller.process.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.common.web.response.ResponseMessage;
import ptithcm.itmc.taskracer.controller.dto.team.CreateNewTeamRequest;
import ptithcm.itmc.taskracer.controller.dto.team.UpdateTeamRequest;
import ptithcm.itmc.taskracer.controller.mapper.team.TeamControllerMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.TeamService;

import java.util.UUID;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final TeamControllerMapper teamControllerMapper;
    private final AuthHelper authHelper;

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
    public ResponseEntity<ResponseAPI<?>> getAllTeam(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        var teams = teamService.getAllTeam(page, size);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(teams)
                .build();
        return ResponseEntity.ok(resp);
    }

    @PostMapping("team")
    public ResponseEntity<ResponseAPI<?>> createTeam(@RequestBody CreateNewTeamRequest request) {

        var teamData = teamControllerMapper.toDto(request);
        var getUser = authHelper.getUser();
        var createTeamData = teamService.createNewTeam(teamData, getUser.getId());
        var toRespData = teamControllerMapper.toDomain(createTeamData);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(toRespData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("team/{slug}")
    public ResponseEntity<ResponseAPI<?>> updateTeam(@PathVariable String slug, @RequestBody UpdateTeamRequest updateRequest) {
        var updateData = teamControllerMapper.toDto(updateRequest);
        var getUser = authHelper.getUser();
        var createTeamData = teamService.updateTeam(slug, updateData, getUser.getId());
        var toRespData = teamControllerMapper.toDomain(createTeamData);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(toRespData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @DeleteMapping("team/{slug}")
    public ResponseEntity<ResponseAPI<?>> deleteTeam(@PathVariable String slug) {
        var getUser = authHelper.getUser();
        teamService.deleteTeam(slug, getUser.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Delete team successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/remove/{userId}")
    public ResponseEntity<ResponseAPI<?>> removeUserFromTeam(@PathVariable String slug, @PathVariable UUID userId) {
        var getUser = authHelper.getUser();
        teamService.removeUserFromTeam(slug, getUser.getId(), userId);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Remove user from team: " + slug + " successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/invite/{userId}")
    public ResponseEntity<ResponseAPI<?>> inviteUserToTeam(@PathVariable String slug, @PathVariable UUID userId) {
        var getUser = authHelper.getUser();
        teamService.inviteUserToTeam(slug, getUser.getId(), userId);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Invite user to team: " + slug + " successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/accept")
    public ResponseEntity<ResponseAPI<?>> acceptUserToTeam(@PathVariable String slug) {
        var getUser = authHelper.getUser();
        teamService.acceptInvite(slug, getUser.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Accept invite to team: " + slug + " successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/reject")
    public ResponseEntity<ResponseAPI<?>> rejectUserToTeam(@PathVariable String slug) {
        var getUser = authHelper.getUser();
        teamService.rejectInvite(slug, getUser.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Reject invite to team: " + slug + " successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/leave")
    public ResponseEntity<ResponseAPI<?>> leaveTeam(@PathVariable String slug) {
        var getUser = authHelper.getUser();
        teamService.leaveTeam(slug, getUser.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(new ResponseMessage("Leave team: " + slug + " successful"))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/request")
    public ResponseEntity<ResponseAPI<?>> requestJoinTeam(@PathVariable String slug) {
        var getUser = authHelper.getUser();
        teamService.requestToJoinTeam(slug, getUser.getId());
        var resp = ResponseAPI.builder()
               .code(ResponseCode.SUCCESS.getCode())
               .message(ResponseCode.SUCCESS.getMessage())
               .status(true)
               .data(new ResponseMessage("Request join team: " + slug + " successful"))
               .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("team/{slug}/accept-request/{userId}")
    public ResponseEntity<ResponseAPI<?>> acceptRequestJoinTeam(@PathVariable String slug, @PathVariable UUID userId) {
        var getUser = authHelper.getUser();
        teamService.acceptRequest(slug, getUser.getId(), userId);
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(new ResponseMessage("Accept request join team: " + slug + " successful"))
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
