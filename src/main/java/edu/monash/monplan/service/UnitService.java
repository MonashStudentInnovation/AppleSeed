package edu.monash.monplan.service;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.repository.UnitRepository;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.InsufficientResourcesException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
/**
 * Unit Service
 */
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Save Units
     * @param unit
     * @return
     */
    private Unit save(final Unit unit) {
        if (unit.getId() == null){
            // We initialise a new UUID if we dont have a UUID for the new Unit.
            unit.init();
        }
        return unitRepository.put(unit);
    }

    public Unit createUnit(Unit unit) throws FailedOperationException {
        // Check if given unit has an id.
        if (unit.getId() != null) {
            // Ensure that if id is provided, it is not already in use.
            Unit unitWithId = unitRepository.get(unit.getId());

            // If unitWithId is not null, that means the Id is in use.
            if (unitWithId != null) {
                throw new FailedOperationException();
            }
        }
        return this.save(unit);
    }

    public List<Unit> listAllUnits() {
        return unitRepository.list(5000);
    }

    public Unit getUnitByUnitCode(String unitCode){
        List<Unit> units = unitRepository.getByField("unitCode", unitCode );

        // The search result is either 0 or 1 units.
        if (units.size() == 1) {
            return units.get(0);
        }
        return null;
    }

    public List<Unit> getUnitsByUnitName(String unitName) {return unitRepository.listByUnitName(unitName);}

    public Unit updateUnitByUnitId(Unit unit) throws InsufficientResourcesException, NotFoundException {
        if (unit.getId() == null) {
            throw new InsufficientResourcesException();
        }
        if (unitRepository.get(unit.getId()) == null) {
            throw new NotFoundException();
        }
        return unitRepository.put(unit);
    }

    @Async
    public void deleteUnit(String id) throws NotFoundException, FailedOperationException {
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
}
