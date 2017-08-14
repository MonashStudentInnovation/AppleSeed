package edu.monash.monplan.service;

import com.googlecode.objectify.Work;
import edu.monash.monplan.model.Unit;
import edu.monash.monplan.repository.UnitRepository;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class UnitService {
    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit getByUnitCode(String unitCode){
        return unitRepository.getByField("unitCode", unitCode ).get(0);
    }

    public Unit addUnit(Unit unit){
        return this.save(unit);
    }

    private Unit save(final Unit unit) {
        if (unit.getId() == null){
            // we initialise a new UUID if we dont have a UUID for the new Unit
            unit.init();
        }
        return unitRepository.put(unit);
    }

}
