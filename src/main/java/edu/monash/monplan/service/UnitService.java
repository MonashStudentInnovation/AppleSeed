package edu.monash.monplan.service;

import edu.monash.monplan.model.Unit;
import org.monplan.abstraction_layer.MonPlanRepository;
import org.monplan.abstraction_layer.MonPlanService;
import org.springframework.stereotype.Service;

@Service
public class UnitService extends MonPlanService<Unit> {

    public UnitService(MonPlanRepository<Unit> repository) {
        super(repository);
    }
}
