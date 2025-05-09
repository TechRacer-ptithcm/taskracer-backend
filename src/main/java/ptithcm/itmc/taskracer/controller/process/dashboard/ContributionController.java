package ptithcm.itmc.taskracer.controller.process.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.mapper.dashboard.DashboardControllerMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.DashboardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("dashboard")
public class ContributionController {
    private final AuthHelper authHelper;
    private final DashboardService dashboardService;
    private final DashboardControllerMapper mapper;

    @GetMapping("contribution")
    public ResponseEntity<ResponseAPI<?>> getContributionTable() {
        var userData = authHelper.getUser();
        var data = dashboardService.getContributionTable(userData.getId());
        var formatData = mapper.toDomain(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
