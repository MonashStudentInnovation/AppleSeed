package edu.monash.monplan.service;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.repository.MonPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitService extends MonPlanService<Unit> {

    public UnitService(MonPlanRepository<Unit> repository) {
        super(repository);
    }
}
