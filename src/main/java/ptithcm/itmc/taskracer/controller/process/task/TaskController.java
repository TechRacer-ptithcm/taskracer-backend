package ptithcm.itmc.taskracer.controller.process.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.service.process.task.TaskService;

@Controller
@RequiredArgsConstructor
@RequestMapping("content")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("tasks")
    public ResponseEntity<ResponseAPI<?>> getAllTask(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        var data = taskService.getAllTask(pageable);
        var result = ResponseAPI.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .data(data)
                .build();
        return ResponseEntity.ok(result);

    }
}
