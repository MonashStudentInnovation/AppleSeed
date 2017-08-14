package edu.monash.monplan.service;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.threewks.gaetools.logger.Logger;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class TaskService {

    private final Queue queue;

    public TaskService(Queue queue) {
        this.queue = queue;
    }

    public void queueLogMessage(String message) {
        Logger.info("Queueing log message: %s", message);
        queueTask(TaskOptions.Builder.withUrl("/task/log-message").param("message", message));
    }

    private void queueTask(TaskOptions taskOptions) {
        TaskHandle taskHandle = queue.add(ofy().getTransaction(), taskOptions);
        Logger.info("Queued task %s on queue %s", taskHandle.getName(), queue.getQueueName());
    }

}
