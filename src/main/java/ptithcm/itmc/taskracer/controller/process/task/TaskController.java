package ptithcm.itmc.taskracer.controller.process.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;
import ptithcm.itmc.taskracer.controller.dto.task.CreateTaskRequest;
import ptithcm.itmc.taskracer.controller.dto.task.HandleUserInTaskRequest;
import ptithcm.itmc.taskracer.controller.dto.task.UpdateTaskRequest;
import ptithcm.itmc.taskracer.controller.mapper.task.TaskControllerMapper;
import ptithcm.itmc.taskracer.helper.AuthHelper;
import ptithcm.itmc.taskracer.service.process.task.ITaskService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("content")
public class TaskController {
    private final ITaskService taskService;
    private final TaskControllerMapper taskControllerMapper;
    private final AuthHelper authHelper;

    @PostMapping("task")
    public ResponseEntity<ResponseAPI<?>> createNewTask(@RequestBody CreateTaskRequest request) {
        var getUser = authHelper.getUser();
        var data = taskService.createTask(taskControllerMapper.toTaskDto(request), getUser.getId());
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("tasks")
    public ResponseEntity<ResponseAPI<?>> getAllTasksThrowUserId() {
        var userData = authHelper.getUser();
        var data = taskService.getAllTask(userData.getId());
        var formatData = taskControllerMapper.toListTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("task")
    public ResponseEntity<ResponseAPI<?>> getTaskByTaskId(@RequestParam(value = "taskId") String taskId) {
        var userData = authHelper.getUser();
        var data = taskService.getTaskById(UUID.fromString(taskId), userData.getId());
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("task")
    public ResponseEntity<ResponseAPI<?>> updateTask(@RequestParam(value = "taskId") String taskId, @RequestBody UpdateTaskRequest request) {
        var userData = authHelper.getUser();
        var data = taskService.updateTask(taskControllerMapper.toTaskDto(request), UUID.fromString(taskId), userData.getId());
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("task")
    public ResponseEntity<ResponseAPI<?>> deleteTask(@RequestParam(value = "taskId") String taskId) {
        var userData = authHelper.getUser();
        var data = taskService.deleteTask(UUID.fromString(taskId), userData.getId());
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("task/assign-user")
    public ResponseEntity<ResponseAPI<?>> assignUserToTask(@RequestBody HandleUserInTaskRequest request) {
        var data = taskService.addUserToTask(taskControllerMapper.toHandleUserDto(request));
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("task/remove-user")
    public ResponseEntity<ResponseAPI<?>> removeUserFromTask(@RequestBody HandleUserInTaskRequest request) {
        var data = taskService.removeUserFromTask(taskControllerMapper.toHandleUserDto(request));
        var formatData = taskControllerMapper.toTaskResponse(data);
        var result = ResponseAPI.builder()
                .data(formatData)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .status(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
