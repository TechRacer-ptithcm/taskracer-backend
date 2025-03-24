package ptithcm.itmc.taskracer.controller.process.ranking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("social/ranking")
public class RankingController {
    @GetMapping("leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        return null;
    }

}
