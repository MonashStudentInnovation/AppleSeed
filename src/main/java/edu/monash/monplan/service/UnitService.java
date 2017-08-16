package edu.monash.monplan.service;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Unit;
import edu.monash.monplan.repository.UnitRepository;
import org.monplan.FailedOperationException;
import org.monplan.NotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
/**
 * Unit Service
 */
public class UnitService {



    private final UnitRepository unitRepository;

    /**
     * Save Units
     * @param unit
     * @return
     */
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
        List<Unit> units = unitRepository.getByField("unitCode", unitCode );

        // the search result is either 0 or 1 units
        if (units.size() == 1) {
            return unitRepository.getByField("unitCode", unitCode ).get(0);
        }
        return null;
    }

    public Unit addUnit(Unit unit){
        return this.save(unit);
    }


    public List<Unit> listAllUnits() {
        return unitRepository.list(5000);
    }

    @Async
    public void delete(String id){
        Unit unit = unitRepository.get(id);
        if (unit == null) {
            throw new NotFoundException();
        }

        unitRepository.deleteByKey(id);

        unit = unitRepository.get(id);
        if (unit != null) {
            throw new FailedOperationException();
        }
    }

    public Unit find(String id){
        return unitRepository.get(id);
    }
    
    public List<Unit> getByName(String unitName) {return unitRepository.listByUnitName(unitName);}
}
