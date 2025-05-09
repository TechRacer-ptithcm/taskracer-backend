package ptithcm.itmc.taskracer.controller.process.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.mapper.ranking.RankingMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.RankingService;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;
    private final RankingMapper mapper;
    private final AuthHelper authHelper;

    @GetMapping("leaderboard")
    public ResponseEntity<ResponseAPI<?>> getLeaderboard() {
        var data = rankingService.getList();
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(mapper.toDomain(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("ranking/current-top")
    public ResponseEntity<ResponseAPI<?>> getCurrentTop() {
        var userData = authHelper.getUser();
        var data = rankingService.getCurrentTop(userData.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(mapper.toDomainInteger(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("ranking/percent-top")
    public ResponseEntity<ResponseAPI<?>> getPercentTop() {
        var userData = authHelper.getUser();
        var data = rankingService.getPercentTop(userData.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(mapper.toDomainDouble(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("ranking/current-data")
    public ResponseEntity<ResponseAPI<?>> getCurrent() {
        var userData = authHelper.getUser();
        var data = rankingService.getOne(userData.getId());
        var resp = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(mapper.toDomain(data))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
