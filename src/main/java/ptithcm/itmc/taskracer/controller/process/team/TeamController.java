package ptithcm.itmc.taskracer.controller.process.team;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.service.process.team.ITeamService;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
public class TeamController {
    private final ITeamService teamService;

    @GetMapping("team/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable String id) {
        return null;
    }

    @GetMapping("teams")
    public ResponseEntity<?> getAllTeam() {
        return null;
    }

    @PostMapping("team")
    public ResponseEntity<?> createTeam() {
        return null;
    }

    @PutMapping("team/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("team/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/remove/{id}")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/invite/{id}")
    public ResponseEntity<?> addUserToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/ask/{id}")
    public ResponseEntity<?> addTaskToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/accept/{id}")
    public ResponseEntity<?> acceptTaskToTeam(@PathVariable String id) {
        return null;
    }

    @PostMapping("team/reject/{id}")
    public ResponseEntity<?> rejectTaskToTeam(@PathVariable String id) {
        return null;
    }
}
