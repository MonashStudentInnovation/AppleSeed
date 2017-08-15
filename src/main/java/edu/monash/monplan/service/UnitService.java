package edu.monash.monplan.service;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.repository.UnitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {
    private final UnitRepository unitRepository;

    private Unit save(final Unit unit) {
        if (unit.getId() == null){
            // we initialise a new UUID if we dont have a UUID for the new Unit
            unit.init();
        }
        return unitRepository.put(unit);
    }

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit getByUnitCode(String unitCode){
        return unitRepository.getByField("unitCode", unitCode ).get(0);
    }

    public Unit addUnit(Unit unit){
        return this.save(unit);
    }


    public List<Unit> listAllUnits() {
        return unitRepository.list(5000);
    }

    @Async
    public void delete(String id){
        unitRepository.deleteByKey(id);
    }

    public Unit find(String id){
        return unitRepository.get(id);
    }
}
