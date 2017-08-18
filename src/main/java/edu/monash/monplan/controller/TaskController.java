package edu.monash.monplan.controller;

import com.threewks.gaetools.logger.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    @RequestMapping(path = "log-message", method = RequestMethod.POST)
    public void logMessage(String message) {
        Logger.info("\n\n\n LOG MESSAGE: %s\n\n\n", message);
    }

}
