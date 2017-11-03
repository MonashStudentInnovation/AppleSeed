package edu.monash.monplan.service;

import edu.monash.monplan.model.Log;
import org.monplan.abstraction_layer.MonPlanRepository;
import org.monplan.abstraction_layer.MonPlanService;
import org.springframework.stereotype.Service;

@Service
public class LogService extends MonPlanService<Log> {

    public LogService(MonPlanRepository<Log> repository) {
        super(repository);
    }


}
