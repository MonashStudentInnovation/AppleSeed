package hello.controller;

import com.google.common.collect.ImmutableMap;
import hello.controller.request.MessageTaskRequest;
import hello.service.TaskService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TaskService taskService;

    public AdminController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String index() {
        return "Welcome to admin page";
    }

    @RequestMapping(path = "queue-log-message", method = RequestMethod.POST)
    public Map<String, String> queueLogMessage(@RequestBody @Valid MessageTaskRequest request) {

        taskService.queueLogMessage(request.getMessage());

        return ImmutableMap.of("message", "Task queued");
    }

}
