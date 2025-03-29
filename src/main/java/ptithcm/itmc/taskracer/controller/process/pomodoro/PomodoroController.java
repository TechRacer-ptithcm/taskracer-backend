package ptithcm.itmc.taskracer.controller.process.pomodoro;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.process.pomodoro.IPomodoroService;

@RestController
@RequestMapping("pomodoro")
@RequiredArgsConstructor
public class PomodoroController {
    private final AuthHelper authHelper;
    private final IPomodoroService pomodoroService;

    @PostMapping("start")
    public ResponseEntity<ResponseAPI<?>> startPomodoro() {
        var userData = authHelper.getUser();
        var data = pomodoroService.startPomodoro(userData.getId());
        var result = ResponseAPI.builder()
                .data(data)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
