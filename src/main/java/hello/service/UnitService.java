package hello.service;

import hello.model.Unit;
import hello.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {
    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit getByUnitCode(String unitCode){
        return unitRepository.getByField("unitCode", unitCode ).get(0);
    }

    public void addUnit(Unit unit){
        unitRepository.put(unit);
    }
}
